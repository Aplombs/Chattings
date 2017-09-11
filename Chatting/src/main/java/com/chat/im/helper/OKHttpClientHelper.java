package com.chat.im.helper;

import android.os.Build;
import android.text.TextUtils;

import com.chat.im.constant.Constants;
import com.chat.im.cookie.PersistentCookieStore;
import com.chat.im.db.bean.ContactInfo;
import com.chat.im.db.dao.ContactInfoDao;
import com.chat.im.jsonbean.CheckPhoneRequest;
import com.chat.im.jsonbean.CheckPhoneResponse;
import com.chat.im.jsonbean.GetAllContactResponse;
import com.chat.im.jsonbean.GetTokenRequest;
import com.chat.im.jsonbean.GetTokenResponse;
import com.chat.im.jsonbean.GetUserInfoByIdResponse;
import com.chat.im.jsonbean.GetUserInfoByPhoneResponse;
import com.chat.im.jsonbean.LoginRequest;
import com.chat.im.jsonbean.LoginResponse;
import com.chat.im.jsonbean.RegisterRequest;
import com.chat.im.jsonbean.RegisterResponse;
import com.chat.im.jsonbean.SendCodeRequest;
import com.chat.im.jsonbean.SendCodeResponse;
import com.chat.im.jsonbean.VerifyCodeRequest;
import com.chat.im.jsonbean.VerifyCodeResponse;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.RongIMClient;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OKHttpClient
 */

public class OKHttpClientHelper {

    private static OkHttpClient mOkHttpClient;
    private static PersistentCookieStore mCookieStore;
    private static OKHttpClientHelper okHttpClientHelper;
    private final String TAG = "[OKHttpClientHelper] ";
    private ResponseListener mResponseListener;

    private OKHttpClientHelper() {
    }

    public static OKHttpClientHelper getInstance() {
        if (null == okHttpClientHelper) {
            synchronized (OKHttpClientHelper.class) {
                if (null == okHttpClientHelper) {
                    try {
                        okHttpClientHelper = new OKHttpClientHelper();
                        mCookieStore = new PersistentCookieStore();
                        mOkHttpClient = new OkHttpClient.Builder()
                                .hostnameVerifier(getHostnameVerifier())
                                .sslSocketFactory(getSSLSocketFactory())
                                .addInterceptor(getInterceptor())
                                .readTimeout(1, TimeUnit.MINUTES)
                                .build();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return okHttpClientHelper;
    }

    private static Interceptor getInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                List<Cookie> list = mCookieStore.getCookies();
                StringBuilder cookieSB = new StringBuilder();
                if (list != null && list.size() > 0) {
                    for (Cookie cookie : list) {
                        cookieSB.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
                    }
                }

                Request.Builder newBuilder = chain.request().newBuilder();
                newBuilder.addHeader("User-Agent", "SealTalk v" + "1.2.3" + " (Android; targetsdkversion " + Build.VERSION.SDK_INT + ";)");
                //newBuilder.addHeader("Accept-Encoding", "gzip");
                newBuilder.addHeader("Cookie", cookieSB.toString());
                return chain.proceed(newBuilder.build());
            }
        };
    }

    private static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            X509TrustManager x509TrustManager = new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    X509Certificate[] x509Certificates = new X509Certificate[0];
                    return x509Certificates;
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                }
            };
            sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }

    public static void clearCache() {
        okHttpClientHelper = null;
    }

    /**
     * 给要注册的手机号发送验证码(先验证当前手机号是否可用)
     *
     * @param phone 要注册的手机号
     */
    public void sendVerificationCode(final String phone) {
        String url = RequestURLHelper.getInstance().getCheckPhoneAvailableUrl();

        String json = JsonHelper.beanToJson(new CheckPhoneRequest(phone, Constants.REGION));
        RequestBody requestBody = getRequestBody(json);

        sendPostRequest(url, requestBody, new CallBack() {
            @Override
            public void onNext(String result) throws Exception {
                LogHelper.e(TAG + "checkPhone onResponse()--->>" + result);
                //调用onNext之前已经判断过result不为null
                CheckPhoneResponse checkPhoneResponse = JsonHelper.jsonToBean(result, CheckPhoneResponse.class);
                if (200 == checkPhoneResponse.getCode()) {
                    boolean isAvailable = checkPhoneResponse.isResult();
                    if (isAvailable) {//手机号可用,进行发送验证码操作
                        sendCode(phone);
                    } else {//手机号已被注册,直接回调UI提示
                        mResponseListener.onFailure(Constants.FAILURE_SEND_CODE, Constants.FAILURE_TYPE_PHONE_USED);
                    }
                } else {
                    mResponseListener.onFailure(Constants.FAILURE_SEND_CODE, Constants.FAILURE_TYPE_OTHER);
                }
            }

            @Override
            public void onError() {
                mResponseListener.onFailure(Constants.FAILURE_SEND_CODE, Constants.FAILURE_TYPE_OTHER);
            }
        });
    }

    /**
     * 验证用户输入的验证码,验证成功直接调注册
     *
     * @param nickName 用户设置的昵称
     * @param phone    用户输入的手机号
     * @param code     用户输入的验证码
     * @param password 用户设置的密码
     */
    public void verifyCode(final String nickName, String phone, String code, final String password) {
        String url = RequestURLHelper.getInstance().getVerifyCodeUrl();

        String json = JsonHelper.beanToJson(new VerifyCodeRequest(Constants.REGION, phone, code));
        RequestBody requestBody = getRequestBody(json);

        sendPostRequest(url, requestBody, new CallBack() {
            @Override
            public void onNext(String result) throws Exception {

                LogHelper.e(TAG + "verifyCode onResponse()--->>" + result);
                //调用onNext之前已经判断过result不为null
                VerifyCodeResponse verifyCodeResponse = JsonHelper.jsonToBean(result, VerifyCodeResponse.class);
                int verifyCodeResponseCode = verifyCodeResponse.getCode();

                if (200 == verifyCodeResponseCode) {
                    String verification_token = verifyCodeResponse.getResult().getVerification_token();
                    if (!TextUtils.isEmpty(verification_token)) {//验证码正确,开始注册
                        register(nickName, verification_token, password);
                    } else {
                        //注册失败,稍后重试
                        mResponseListener.onFailure(Constants.FAILURE_REGISTER, Constants.FAILURE_TYPE_OTHER);
                    }
                } else {//验证码错误或者验证码已过期
                    if (1000 == verifyCodeResponseCode) {//验证码错误
                        mResponseListener.onFailure(Constants.FAILURE_REGISTER, Constants.FAILURE_TYPE_VERIFY_CODE_WRONG);
                    } else if (2000 == verifyCodeResponseCode) {//验证码过期
                        mResponseListener.onFailure(Constants.FAILURE_REGISTER, Constants.FAILURE_TYPE_VERIFY_CODE_OVERDUE);
                    } else {
                        mResponseListener.onFailure(Constants.FAILURE_REGISTER, Constants.FAILURE_TYPE_OTHER);
                    }
                }
            }

            @Override
            public void onError() {
                //注册失败,稍后重试
                mResponseListener.onFailure(Constants.FAILURE_REGISTER, Constants.FAILURE_TYPE_OTHER);
            }
        });
    }

    /**
     * 登录
     *
     * @param phone    手机号
     * @param password 密码
     */
    public void login(String phone, String password) {
        String url = RequestURLHelper.getInstance().getLoginUrl();

        String json = JsonHelper.beanToJson(new LoginRequest(Constants.REGION, phone, password));
        RequestBody requestBody = getRequestBody(json);

        sendPostRequest(url, requestBody, new CallBack() {
            @Override
            public void onNext(String result) throws Exception {
                LogHelper.e(TAG + "login onResponse()--->>" + result);
                //调用onNext之前已经判断过result不为null
                LoginResponse loginResponse = JsonHelper.jsonToBean(result, LoginResponse.class);
                int loginResponseCode = loginResponse.getCode();
                //登录融云后台成功之后,去调用接口获取token(本该登录自己的应用后台,然后登录成功的时候自己后台将token返回)
                //由于没有自己的后台,所以只能借助登录到融云官方demo的后台,然后客户端自己调用getToken接口获取token,然后
                //调用RongIMClient.connect()方法,再持续后面的操作
                if (200 == loginResponseCode) {
                    String portraitUri = SpHelper.getInstance().get(Constants.SP_LOGIN_HEAD_URI, "");
                    getToken(loginResponse.getResult().getId(), portraitUri);
                } else {
                    if (100 == loginResponseCode || 1000 == loginResponseCode) {//手机号错误或者密码错误
                        mResponseListener.onFailure(Constants.FAILURE_LOGIN, Constants.FAILURE_TYPE_LOGIN_PHONE_OR_PASSWORD_WRONG);
                    } else {
                        //登录失败,稍后重试
                        mResponseListener.onFailure(Constants.FAILURE_LOGIN, Constants.FAILURE_TYPE_OTHER);
                    }
                }
            }

            @Override
            public void onError() {
                //登录失败,稍后重试
                mResponseListener.onFailure(Constants.FAILURE_LOGIN, Constants.FAILURE_TYPE_OTHER);
            }
        });
    }

    /**
     * 获取所有联系人
     */
    public void getAllContact() throws Exception {
        String url = RequestURLHelper.getInstance().getAllContactUrl();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = mOkHttpClient.newCall(request).execute();

        if (response != null) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String result = responseBody.string();
                if (!TextUtils.isEmpty(result)) {
                    LogHelper.e(TAG + "getAllContact onResponse()--->>" + result);
                    GetAllContactResponse allContactResponse = JsonHelper.jsonToBean(result, GetAllContactResponse.class);
                    if (allContactResponse != null && allContactResponse.getCode() == 200) {
                        List<GetAllContactResponse.ResultEntity> list = allContactResponse.getResult();
                        if (list != null && list.size() > 0) {
                            List<ContactInfo> contactInfoList = new ArrayList<>();
                            for (GetAllContactResponse.ResultEntity resultEntity : list) {
                                GetAllContactResponse.ResultEntity.UserEntity userEntity = resultEntity.getUser();
                                String userId = userEntity.getId();
                                String nickname = userEntity.getNickname();
                                String remarkName = resultEntity.getDisplayName();
                                String userHeadUri = userEntity.getPortraitUri();
                                String region = userEntity.getRegion();
                                String phone = userEntity.getPhone();
                                String showName;
                                String showNameLetter;
                                if (TextUtils.isEmpty(remarkName)) {//备注为空取昵称的首字母
                                    showName = nickname;
                                    showNameLetter = UtilsHelper.getInstance().getFirstLetter(nickname);
                                } else {
                                    showName = remarkName;
                                    showNameLetter = UtilsHelper.getInstance().getFirstLetter(remarkName);
                                }

                                ContactInfo contactInfo = new ContactInfo(userId, region, phone, userHeadUri, nickname, remarkName, showName, showNameLetter);
                                contactInfoList.add(contactInfo);
                            }
                            ContactInfoDao contactInfoDao = DBHelper.getInstance().getDaoSession().getContactInfoDao();
                            //清空之前的数据,将服务器最新数据插入数据库
                            contactInfoDao.deleteAll();
                            contactInfoDao.insertOrReplaceInTx(contactInfoList);
                        }
                    }
                }
            }
        }
    }

    /**
     * 查询用户信息
     *
     * @param userID 要查询的用户ID
     */
    public void getUserInfoByID(String userID) {
        String userInfoUrl = RequestURLHelper.getInstance().getUserInfoUrl(userID);

        sendGetRequest(userInfoUrl, new CallBack() {
            @Override
            public void onNext(String result) throws Exception {
                LogHelper.e(TAG + "getUserInfoByID onResponse()--->>" + result);
                GetUserInfoByIdResponse userInfoByIdResponse = JsonHelper.jsonToBean(result, GetUserInfoByIdResponse.class);
                int getUserInfoResponseCode = userInfoByIdResponse.getCode();
                if (200 == getUserInfoResponseCode) {
                    mResponseListener.onResponse(Constants.OK_GET_USER_INFO, userInfoByIdResponse);
                } else {
                    mResponseListener.onFailure(Constants.FAILURE_GET_USER_INFO, Constants.FAILURE_TYPE_OTHER);
                }
            }

            @Override
            public void onError() {
                mResponseListener.onFailure(Constants.FAILURE_GET_USER_INFO, Constants.FAILURE_TYPE_OTHER);
            }
        });
    }

    /***
     * 通过手机号搜索好友信息
     * @param region 地域号
     * @param phone 手机号
     */
    public void searchFriendByPhone(String region, String phone) {
        String url = RequestURLHelper.getInstance().getSearchFriendUrl(region, phone);

        sendGetRequest(url, new CallBack() {
            @Override
            public void onNext(String result) throws Exception {
                LogHelper.e(TAG + "searchFriendByPhone onResponse()--->>" + result);
                if ("Unknown user.".equalsIgnoreCase(result)) {
                    mResponseListener.onFailure(Constants.FAILURE_SEARCH_FRIEND, Constants.FAILURE_TYPE_SEARCH_FRIEND_NOT_EXIST);
                    return;
                }
                GetUserInfoByPhoneResponse userInfoByPhoneResponse = JsonHelper.jsonToBean(result, GetUserInfoByPhoneResponse.class);
                int getUserInfoResponseCode = userInfoByPhoneResponse.getCode();
                if (200 == getUserInfoResponseCode) {
                    mResponseListener.onResponse(Constants.OK_SEARCH_FRIEND, userInfoByPhoneResponse);
                } else {
                    mResponseListener.onFailure(Constants.FAILURE_SEARCH_FRIEND, Constants.FAILURE_TYPE_OTHER);
                }
            }

            @Override
            public void onError() {
                mResponseListener.onFailure(Constants.FAILURE_SEARCH_FRIEND, Constants.FAILURE_TYPE_OTHER);
            }
        });
    }

    /************************************************内部使用方法 start***************************************************************************************/

    //发送验证码
    private void sendCode(String phone) {
        String url = RequestURLHelper.getInstance().getSendCodeUrl();

        String json = JsonHelper.beanToJson(new SendCodeRequest(Constants.REGION, phone));
        RequestBody requestBody = getRequestBody(json);

        sendPostRequest(url, requestBody, new CallBack() {
            @Override
            public void onNext(String result) throws Exception {
                LogHelper.e(TAG + "sendCode onResponse()--->>" + result);
                SendCodeResponse sendCodeResponse = JsonHelper.jsonToBean(result, SendCodeResponse.class);
                if (200 == sendCodeResponse.getCode()) {
                    //调用onNext之前已经判断过result不为null
                    mResponseListener.onResponse(Constants.OK_SEND_CODE, sendCodeResponse);
                } else {
                    mResponseListener.onFailure(Constants.FAILURE_SEND_CODE, Constants.FAILURE_TYPE_OTHER);
                }
            }

            @Override
            public void onError() {
                mResponseListener.onFailure(Constants.FAILURE_SEND_CODE, Constants.FAILURE_TYPE_OTHER);
            }
        });
    }

    //注册
    private void register(String nickName, String verifyToken, String password) {
        String url = RequestURLHelper.getInstance().getRegisterUrl();

        String json = JsonHelper.beanToJson(new RegisterRequest(nickName, password, verifyToken));
        RequestBody requestBody = getRequestBody(json);

        sendPostRequest(url, requestBody, new CallBack() {
            @Override
            public void onNext(String result) throws Exception {
                LogHelper.e(TAG + "register onResponse()--->>" + result);
                //调用onNext之前已经判断过result不为null
                RegisterResponse registerResponse = JsonHelper.jsonToBean(result, RegisterResponse.class);
                int registerResponseCode = registerResponse.getCode();

                if (200 == registerResponseCode) {
                    mResponseListener.onResponse(Constants.OK_REGISTER, registerResponse);
                } else {//400--错误的请求 404--token不存在 500--应用服务端内部错误
                    //注册失败,稍后重试
                    mResponseListener.onFailure(Constants.FAILURE_REGISTER, Constants.FAILURE_TYPE_OTHER);
                }
            }

            @Override
            public void onError() {
                //注册失败,稍后重试
                mResponseListener.onFailure(Constants.FAILURE_REGISTER, Constants.FAILURE_TYPE_OTHER);
            }
        });
    }

    //保存cookie
    private void saveCookies(Response response) {
        List<String> values = response.headers().values("Set-Cookie");

        if (values != null) {
            for (int i = 0; i < values.size(); i++) {

                String cookie = values.get(i);
                String[] cookieValues = cookie.split(";");

                for (int j = 0; j < cookieValues.length; j++) {
                    String[] keyPair = cookieValues[j].split("=");
                    String key = keyPair[0].trim();
                    String value = keyPair.length > 1 ? keyPair[1].trim() : "";
                    BasicClientCookie newCookie = new BasicClientCookie(key, value);
                    mCookieStore.addCookie(newCookie);
                }
            }
        }
    }

    // 获取token--先去查用户的昵称
    private void getToken(final String userId, final String portraitUri) {
        String userInfoUrl = RequestURLHelper.getInstance().getUserInfoUrl(userId);

        sendGetRequest(userInfoUrl, new CallBack() {
            @Override
            public void onNext(String result) throws Exception {
                LogHelper.e(TAG + "getToken --->> getUserInfo onResponse()" + result);
                GetUserInfoByIdResponse userInfoByIdResponse = JsonHelper.jsonToBean(result, GetUserInfoByIdResponse.class);
                int getUserInfoResponseCode = userInfoByIdResponse.getCode();
                if (200 == getUserInfoResponseCode) {
                    //保存当前登录用户头像uri
                    SpHelper.getInstance().put(Constants.SP_LOGIN_HEAD_URI, userInfoByIdResponse.getResult().getPortraitUri());
                    String url = RequestURLHelper.getInstance().getTokenUrl();
                    final String nickname = userInfoByIdResponse.getResult().getNickname();
                    String json = new GetTokenRequest(userId, nickname, portraitUri).toString();
                    final RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"), json);

                    String[] token4Headers = UtilsHelper.getInstance().getToken4Headers();
                    String appKey = token4Headers[0];//appKey
                    String nonce = token4Headers[1];//获取随机数
                    String timestamp = token4Headers[2];//获取时间戳
                    String signature = token4Headers[3];//生成本地签名

                    final Request request = new Request.Builder()
                            .url(url)
                            .header("App-Key", appKey)
                            .header("Nonce", nonce)
                            .header("Timestamp", timestamp)
                            .header("Signature", signature)
                            .post(requestBody)
                            .build();

                    sendPostRequest(request, new CallBack() {
                        @Override
                        public void onNext(String result) throws Exception {
                            LogHelper.e(TAG + "getToken onResponse()--->>" + result);

                            final GetTokenResponse getTokenResponse = JsonHelper.jsonToBean(result, GetTokenResponse.class);
                            final int getTokenResponseCode = getTokenResponse.getCode();
                            if (200 == getTokenResponseCode) {
                                getTokenResponse.setNickName(nickname);
                                final String token = getTokenResponse.getToken();
                                RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
                                    @Override
                                    public void onTokenIncorrect() {
                                        LogHelper.e(TAG + "getToken connect onTokenIncorrect");
                                        mResponseListener.onFailure(Constants.FAILURE_LOGIN, Constants.FAILURE_TYPE_OTHER);
                                    }

                                    @Override
                                    public void onSuccess(String loginUserID) {
                                        LogHelper.e(TAG + "getToken connect onSuccess--->>" + loginUserID);
                                        mResponseListener.onResponse(Constants.OK_LOGIN, getTokenResponse);
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                        LogHelper.e(TAG + "getToken connect onTokenIncorrect");
                                        mResponseListener.onFailure(Constants.FAILURE_LOGIN, Constants.FAILURE_TYPE_OTHER);
                                    }
                                });
                            } else {
                                LogHelper.e(TAG + "getToken code!=200");
                                mResponseListener.onFailure(Constants.FAILURE_LOGIN, Constants.FAILURE_TYPE_OTHER);
                            }
                        }

                        @Override
                        public void onError() {
                            LogHelper.e(TAG + "getToken onError()");
                            mResponseListener.onFailure(Constants.FAILURE_LOGIN, Constants.FAILURE_TYPE_OTHER);
                        }
                    });
                } else {
                    LogHelper.e(TAG + "getToken --->> getUserInfo code!=200");
                    mResponseListener.onFailure(Constants.FAILURE_LOGIN, Constants.FAILURE_TYPE_OTHER);
                }
            }

            @Override
            public void onError() {
                LogHelper.e(TAG + "getToken --->> getUserInfo onError()");
                mResponseListener.onFailure(Constants.FAILURE_LOGIN, Constants.FAILURE_TYPE_OTHER);
            }
        });
    }

    /************************************************公共方法 start***************************************************************************************/

    /**
     * 请求服务器接口,get请求
     *
     * @param url      请求URL
     * @param callBack 请求回调
     */
    private void sendGetRequest(String url, final CallBack callBack) {
        final Request request = new Request.Builder()
                .url(url)
                .build();

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                Response response = mOkHttpClient.newCall(request).execute();
                if (response != null) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String result = responseBody.string();
                        if (!TextUtils.isEmpty(result)) {
                            observableEmitter.onNext(result);
                            //保存cookie
                            saveCookies(response);
                        } else {
                            observableEmitter.onError(new Throwable(Constants.RESPONSE_RETURN_NULL));
                        }
                    } else {
                        observableEmitter.onError(new Throwable(Constants.RESPONSE_RETURN_NULL));
                    }
                } else {
                    observableEmitter.onError(new Throwable(Constants.RESPONSE_RETURN_NULL));
                }
            }
        }).subscribeOn(Schedulers.io())//被观察者在子线程
                .observeOn(AndroidSchedulers.mainThread())//观察者在主线程
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) {//onNext
                        try {
                            callBack.onNext(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callBack.onError();
                        }
                    }
                }, new Consumer<Throwable>() {//onError
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onError();
                    }
                });
    }

    /**
     * 请求服务器接口,post请求
     *
     * @param url         请求URL
     * @param requestBody 请求的body
     * @param callBack    请求回调
     */
    private void sendPostRequest(String url, RequestBody requestBody, final CallBack callBack) {
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                Response response = mOkHttpClient.newCall(request).execute();
                if (response != null) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String result = responseBody.string();
                        if (!TextUtils.isEmpty(result)) {
                            observableEmitter.onNext(result);
                            //保存cookie
                            saveCookies(response);
                        } else {
                            observableEmitter.onError(new Throwable(Constants.RESPONSE_RETURN_NULL));
                        }
                    } else {
                        observableEmitter.onError(new Throwable(Constants.RESPONSE_RETURN_NULL));
                    }
                } else {
                    observableEmitter.onError(new Throwable(Constants.RESPONSE_RETURN_NULL));
                }
            }
        }).subscribeOn(Schedulers.io())//被观察者在子线程
                .observeOn(AndroidSchedulers.mainThread())//观察者在主线程
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) {//onNext
                        try {
                            callBack.onNext(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callBack.onError();
                        }
                    }
                }, new Consumer<Throwable>() {//onError
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onError();
                    }
                });
    }

    /**
     * 重载方法,请求服务器接口,post请求
     *
     * @param request  需要独立的请求
     * @param callBack 请求回调
     */
    private void sendPostRequest(final Request request, final CallBack callBack) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                Response response = mOkHttpClient.newCall(request).execute();
                if (response != null) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String result = responseBody.string();
                        if (!TextUtils.isEmpty(result)) {
                            observableEmitter.onNext(result);
                            //保存cookie
                            saveCookies(response);
                        } else {
                            observableEmitter.onError(new Throwable(Constants.RESPONSE_RETURN_NULL));
                        }
                    } else {
                        observableEmitter.onError(new Throwable(Constants.RESPONSE_RETURN_NULL));
                    }
                } else {
                    observableEmitter.onError(new Throwable(Constants.RESPONSE_RETURN_NULL));
                }
            }
        }).subscribeOn(Schedulers.io())//被观察者在子线程
                .observeOn(AndroidSchedulers.mainThread())//观察者在主线程
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) {//onNext
                        try {
                            callBack.onNext(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callBack.onError();
                        }
                    }
                }, new Consumer<Throwable>() {//onError
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onError();
                    }
                });
    }

    /**
     * @param json 要转换成的json
     * @return 获取RequestBody
     */
    private RequestBody getRequestBody(String json) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    }

    /**
     * @param responseListener 暴露给UI请求接口的回调
     */
    public void setResponseListener(ResponseListener responseListener) {
        mResponseListener = null;
        mResponseListener = responseListener;
    }

    /**
     * 请求接口回调,都运行在主线程
     */
    private interface CallBack {

        void onNext(String result) throws Exception;

        void onError();
    }

    /**
     * 请求接口UI回调,都运行在主线程
     */
    public interface ResponseListener {
        /**
         * 请求成功
         *
         * @param requestCode 请求类型(登录,注册,忘记密码...)
         * @param response    请求返回的数据,可选的处理数据
         */
        void onResponse(int requestCode, Object response);

        /**
         * 请求失败
         *
         * @param requestCode 请求类型(登录,注册,忘记密码...)
         * @param statusCode  状态类型(失败原因)
         */
        void onFailure(int requestCode, int statusCode);
    }
}

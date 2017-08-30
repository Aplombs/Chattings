package com.chat.im.helper;

import com.chat.im.constant.Constants;

/**
 * 请求接口的URL
 */

public class RequestURLHelper {

    private static RequestURLHelper urlHelper;

    private RequestURLHelper() {
    }

    public static RequestURLHelper getInstance() {
        if (null == urlHelper) {
            synchronized (RequestURLHelper.class) {
                if (null == urlHelper) {
                    urlHelper = new RequestURLHelper();
                }
            }
        }
        return urlHelper;
    }

    public static void clearCache() {
        urlHelper = null;
    }

    /**
     * @return 获取检查手机号是否可用的URL
     */
    public String getCheckPhoneAvailableUrl() {
        return getURL(Constants.URL_CHECK_PHONE_AVAILABLE);
    }

    /**
     * @return 获取发送验证码的URL
     */
    public String getSendCodeUrl() {
        return getURL(Constants.URL_SEND_CODE);
    }

    /**
     * @return 获取验证用户填写的验证码是否正确的URL
     */
    public String getVerifyCodeUrl() {
        return getURL(Constants.URL_VERIFY_CODE);
    }

    /**
     * @return 获取注册的URL
     */
    public String getRegisterUrl() {
        return getURL(Constants.URL_REGISTER);
    }

    /**
     * @return 获取登录的URL
     */
    public String getLoginUrl() {
        return getURL(Constants.URL_LOGIN);
    }

    /**
     * @return 获取登录的URL
     */
    public String getAllContactUrl() {
        return getURL(Constants.URL_GET_ALL_CONTACT);
    }

    /**
     * @return 获取token的URL
     */
    public String getTokenUrl() {
        return "http://api.cn.ronghub.com/user/getToken.json";
    }

    /**
     * @return 获取查询用户信息的URL
     */
    public String getUserInfoUrl(String userID) {
        return getURL(Constants.URL_GET_USER_INFO + userID);
    }

    private String getURL(String url, String... params) {
        StringBuilder urlBuilder = new StringBuilder(Constants.DOMAIN).append(url);
        if (params != null) {
            for (String param : params) {
                if (!urlBuilder.toString().endsWith("/")) {
                    urlBuilder.append("/");
                }
                urlBuilder.append(param);
            }
        }
        return urlBuilder.toString();
    }
}

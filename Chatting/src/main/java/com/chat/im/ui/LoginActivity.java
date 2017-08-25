package com.chat.im.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;

import com.chat.im.R;
import com.chat.im.constant.Constants;
import com.chat.im.helper.OKHttpClientHelper;
import com.chat.im.helper.SpHelper;
import com.chat.im.helper.UIHelper;
import com.chat.im.helper.UtilsHelper;
import com.chat.im.jsonbean.GetTokenResponse;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener, OKHttpClientHelper.ResponseListener {

    private final String TAG = "[LoginActivity] ";
    private final int START_REGISTER_ACTIVITY_REQUEST_CODE = 1;
    private boolean isLogin = false;//是否正在登录中
    private String password, phone;
    private EditText et_phone, et_password;
    private View tv_login, ll_login_progress, tv_new_user, tv_forget_password;

    @Override
    protected int setCurrentLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected int setLeftReturnButtonVisibility() {
        return View.GONE;
    }

    @Override
    protected int setRightButtonVisibility() {
        return View.GONE;
    }

    @Override
    protected CharSequence setCurrentTitle() {
        return "登录";
    }

    @Override
    protected boolean isNeedTwiceToReturn() {
        return true;
    }

    @Override
    protected void init() {
        et_phone = (EditText) findViewById(R.id.et_phone_login);
        et_password = (EditText) findViewById(R.id.et_password_login);
        tv_login = findViewById(R.id.tv_login);
        ll_login_progress = findViewById(R.id.ll_login_progress);
        tv_new_user = findViewById(R.id.tv_new_user_login);
        tv_forget_password = findViewById(R.id.tv_forget_password_login);

        tv_login.setOnClickListener(this);
        tv_new_user.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = s.toString().trim();
                int length = phone.length();

                if (length == 11) {
                    if (!UtilsHelper.getInstance().isMobile(phone)) {
                        UIHelper.getInstance().toast("请输入正确的手机号");
                        return;
                    }
                    UIHelper.getInstance().hideSoftInput(et_phone);
                    UIHelper.getInstance().showSoftInput(et_password);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        OKHttpClientHelper.getInstance().setResponseListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login://登录
                if (isLogin) return;
                gotoMain();
                //signIn();
                break;
            case R.id.tv_new_user_login://新用户
                newUser();
                break;
            case R.id.tv_forget_password_login://忘记密码
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        OKHttpClientHelper.getInstance().setResponseListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_REGISTER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String phone = data.getStringExtra(Constants.ACTIVITY_RETURN_PHONE);
            String password = data.getStringExtra(Constants.ACTIVITY_RETURN_PASSWORD);
            String id = data.getStringExtra(Constants.ACTIVITY_RETURN_USERID);
            String nickname = data.getStringExtra(Constants.ACTIVITY_RETURN_NICKNAME);
            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(id) && !TextUtils.isEmpty(nickname)) {
                et_phone.setText(phone);
                et_password.setText(password);
                //将当前用户的手机号 密码 id保存
                Map<String, String> map = new HashMap<>();
                map.put(Constants.SP_LOGIN_USERID, id);
                map.put(Constants.SP_LOGIN_PHONE, phone);
                map.put(Constants.SP_LOGIN_PASSWORD, password);
                map.put(Constants.SP_LOGIN_NICKNAME, nickname);
                SpHelper.getInstance().put(map);
            }
        }
    }

    @Override
    public void onResponse(int requestCode, Object response) {
        switch (requestCode) {
            case Constants.OK_LOGIN://登录成功
                GetTokenResponse getTokenResponse = (GetTokenResponse) response;
                //保存用户ID
                String loginUserID = getTokenResponse.getUserId();
                String loginToken = getTokenResponse.getToken();
                String nickName = getTokenResponse.getNickName();
                Map<String, String> map = new HashMap<>();
                map.put(Constants.SP_LOGIN_USERID, loginUserID);
                map.put(Constants.SP_LOGIN_TOKEN, loginToken);
                map.put(Constants.SP_LOGIN_PHONE, phone);
                map.put(Constants.SP_LOGIN_PASSWORD, password);
                map.put(Constants.SP_LOGIN_NICKNAME, nickName);
                SpHelper.getInstance().put(map);

                isLogin = false;
                recoverLoginView();
                UIHelper.getInstance().toast("登录成功");
                gotoMain();
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, int statusCode) {
        switch (requestCode) {
            case Constants.FAILURE_LOGIN://登录失败
                isLogin = false;
                recoverLoginView();
                if (statusCode == Constants.FAILURE_TYPE_LOGIN_PHONE_OR_PASSWORD_WRONG) {//手机号或者密码错误
                    UIHelper.getInstance().toast("手机号或者密码错误,请重新输入");
                } else {//登录失败,未知错误
                    UIHelper.getInstance().toast("登录失败,请稍候重试");
                }
                break;

        }
    }

    //登录
    private void signIn() {
        //检查网络连接
        if (!UIHelper.getInstance().checkNetwork()) {
            return;
        }
        phone = et_phone.getText().toString().trim();
        password = et_password.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            UIHelper.getInstance().toast("请输入登录手机号");
            return;
        }

        if (phone.length() == 11) {
            if (!UtilsHelper.getInstance().isMobile(phone)) {
                UIHelper.getInstance().toast("请输入正确的手机号");
                return;
            }
            UIHelper.getInstance().hideSoftInput(et_phone);
        }

        if (TextUtils.isEmpty(password)) {
            UIHelper.getInstance().toast("请输入登录密码");
            return;
        }

        isLogin = true;
        loginAnimator(tv_login);
        OKHttpClientHelper.getInstance().login(phone, password);
    }

    //注册
    private void newUser() {
        startActivityForResult(new Intent(this, RegisterActivity.class), START_REGISTER_ACTIVITY_REQUEST_CODE);
    }

    //打开主界面
    private void gotoMain() {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    //登录中动画
    private void loginAnimator(final View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(objectAnimator);
        animatorSet.start();

        ll_login_progress.setVisibility(View.VISIBLE);
        tv_login.setVisibility(View.GONE);
    }

    //登录失败的时候登录按钮再次展示并且隐藏掉登录进度条
    private void recoverLoginView() {
        if (tv_login.getVisibility() == View.GONE) {
            ll_login_progress.setVisibility(View.GONE);
            tv_login.setVisibility(View.VISIBLE);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(ObjectAnimator.ofFloat(tv_login, "scaleX", 0f, 1f));
        animatorSet.start();
    }
}

package com.chat.im.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;

import com.chat.im.R;
import com.chat.im.constant.Constants;
import com.chat.im.helper.LogHelper;
import com.chat.im.helper.OKHttpClientHelper;
import com.chat.im.helper.UIHelper;
import com.chat.im.helper.UtilsHelper;
import com.chat.im.jsonbean.RegisterResponse;

/**
 * 注册界面
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener, OKHttpClientHelper.ResponseListener {

    private final String TAG = "[RegisterActivity] ";
    private Button bt_sendCode;
    private boolean isSendVerifyCode;//是否发送过验证码
    private boolean isRegistering;//是否正在注册
    private CountDownTimer mCountDownTimer;
    private View tv_register, ll_register_progress;
    private EditText et_nickname, et_phone, et_code, et_password;
    private String mPhone, mVerifyCode, mNickName, mPassword;
    private AnimatorSet mAnimatorSet;

    @Override
    protected int setCurrentLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {
        et_nickname = (EditText) findViewById(R.id.et_nickname_register);
        et_phone = (EditText) findViewById(R.id.et_phone_register);
        et_code = (EditText) findViewById(R.id.et_code_register);
        et_password = (EditText) findViewById(R.id.et_password_register);
        bt_sendCode = (Button) findViewById(R.id.bt_sendCode_register);
        tv_register = findViewById(R.id.tv_register);
        ll_register_progress = findViewById(R.id.ll_register_progress);

        bt_sendCode.setOnClickListener(this);
        tv_register.setOnClickListener(this);

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = s.toString().trim();
                if (phone.length() == 11) {
                    if (!UtilsHelper.getInstance().isMobile(phone)) {
                        bt_sendCode.setEnabled(false);
                        UIHelper.getInstance().toast("请输入正确的手机号");
                        return;
                    }
                    UIHelper.getInstance().hideSoftInput(et_phone);
                    UIHelper.getInstance().showSoftInput(et_code);
                    bt_sendCode.setEnabled(true);
                } else {
                    bt_sendCode.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String code = s.toString().trim();
                if (code.length() == 6) {
                    UIHelper.getInstance().hideSoftInput(et_code);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        OKHttpClientHelper.getInstance().setResponseListener(this);
    }

    @Override
    protected int setLeftReturnButtonVisibility() {
        return View.VISIBLE;
    }

    @Override
    protected int setRightButtonVisibility() {
        return View.GONE;
    }

    @Override
    protected CharSequence setCurrentTitle() {
        return "注册";
    }

    @Override
    protected boolean isNeedTwiceToReturn() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sendCode_register://发送验证码
                sendCode();
                break;
            case R.id.tv_register://注册
                if (isRegistering) return;
                register();
                break;
        }
    }

    @Override
    protected void onResume() {
        LogHelper.e(TAG + "onResume()");
        super.onResume();
        OKHttpClientHelper.getInstance().setResponseListener(this);
    }

    @Override
    protected void onStop() {
        LogHelper.e(TAG + "onStop()");
        super.onStop();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    @Override
    protected void onDestroy() {
        LogHelper.e(TAG + "onDestroy()");
        super.onDestroy();
    }

    //发送验证码
    private void sendCode() {
        //检查网络连接
        if (!UIHelper.getInstance().checkNetwork()) {
            return;
        }
        String phone = et_phone.getText().toString().trim();
        bt_sendCode.setEnabled(false);
        UIHelper.getInstance().hideSoftInput(et_phone);
        OKHttpClientHelper.getInstance().sendVerificationCode(phone);
    }

    //注册
    private void register() {
        //检查网络连接
        if (!UIHelper.getInstance().checkNetwork()) {
            return;
        }

        mNickName = et_nickname.getText().toString().trim();
        mPhone = et_phone.getText().toString().trim();
        mVerifyCode = et_code.getText().toString().trim();
        mPassword = et_password.getText().toString().trim();

        if (TextUtils.isEmpty(mNickName)) {
            UIHelper.getInstance().toast("请设置您的昵称");
            return;
        }
        if (TextUtils.isEmpty(mVerifyCode)) {
            UIHelper.getInstance().toast("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(mPassword)) {
            UIHelper.getInstance().toast("请设置您的登录密码");
            return;
        }
        if (!isSendVerifyCode) {//检查是否向服务器发送验证码
            UIHelper.getInstance().toast("请获取短信验证码");
            return;
        }
        if (mPassword.contains(" ")) {
            UIHelper.getInstance().toast("密码不能包含空格");
        }
        isRegistering = true;
        registerAnimator(tv_register);
        OKHttpClientHelper.getInstance().verifyCode(mNickName, mPhone, mVerifyCode, mPassword);
    }

    //启动注册中动画
    private void registerAnimator(final View view) {

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.0f);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(1000);
        mAnimatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorSet.playTogether(objectAnimator);
        mAnimatorSet.start();

        ll_register_progress.setVisibility(View.VISIBLE);
        tv_register.setVisibility(View.GONE);
    }

    //注册失败的时候注册按钮再次展示并且隐藏掉注册进度条
    private void recoverRegisterView() {
        if (tv_register.getVisibility() == View.GONE) {
            ll_register_progress.setVisibility(View.GONE);
            tv_register.setVisibility(View.VISIBLE);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(ObjectAnimator.ofFloat(tv_register, "scaleX", 0f, 1f));
        animatorSet.start();
    }

    @Override
    public void onResponse(int requestCode, Object response) {
        switch (requestCode) {
            case Constants.OK_SEND_CODE://验证码发送成功
                isSendVerifyCode = true;//验证码发送成功,设置已经发送过验证码
                UIHelper.getInstance().toast("验证码已发送,请注意查收");
                countDownTimer();
                break;
            case Constants.OK_REGISTER://注册成功,跳转至登录界面
                isRegistering = false;
                recoverRegisterView();
                UIHelper.getInstance().toast("注册成功");
                RegisterResponse registerResponse = (RegisterResponse) response;
                Intent data = new Intent();
                data.putExtra(Constants.ACTIVITY_RETURN_PHONE, mPhone);
                data.putExtra(Constants.ACTIVITY_RETURN_PASSWORD, mPassword);
                data.putExtra(Constants.ACTIVITY_RETURN_NICKNAME, mNickName);
                data.putExtra(Constants.ACTIVITY_RETURN_USERID, registerResponse.getResult().getId());
                setResult(RESULT_OK, data);
                new Handler().postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        RegisterActivity.this.finish();
                    }
                }, 500);
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, int statusCode) {
        switch (requestCode) {
            case Constants.FAILURE_SEND_CODE://验证码发送失败
                if (Constants.FAILURE_TYPE_PHONE_USED == statusCode) {//手机号已被注册
                    UIHelper.getInstance().toast("手机号已被注册,请直接登录");
                } else {//发送失败
                    isSendVerifyCode = false;//验证码发送失败,设置没有发送过验证码
                    UIHelper.getInstance().toast("发送失败,稍后重试");
                    bt_sendCode.setText("发送验证码");
                    bt_sendCode.setEnabled(true);
                }
                break;
            case Constants.FAILURE_REGISTER://注册失败
                isRegistering = true;
                recoverRegisterView();//重新展示注册按钮
                if (statusCode == Constants.FAILURE_TYPE_VERIFY_CODE_WRONG) {//验证码错误
                    UIHelper.getInstance().toast("验证码不正确");
                } else if (statusCode == Constants.FAILURE_TYPE_VERIFY_CODE_OVERDUE) {//验证码已过期
                    UIHelper.getInstance().toast("验证码已过期,请重新发送");
                } else {//注册失败--未知错误
                    UIHelper.getInstance().toast("注册失败,稍后重试");
                }
                break;
        }
    }

    //已发送验证码倒计时
    private void countDownTimer() {
        mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {
            //正在倒计时
            @Override
            public void onTick(long millisUntilFinished) {
                bt_sendCode.setText(millisUntilFinished / 1000 + "s");
                bt_sendCode.setEnabled(false);
            }

            //倒计时结束
            @Override
            public void onFinish() {
                bt_sendCode.setText("发送验证码");
                bt_sendCode.setEnabled(true);
            }
        }.start();
    }
}

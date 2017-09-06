package com.chat.im.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.chat.im.R;
import com.chat.im.constant.Constants;
import com.chat.im.helper.LogHelper;
import com.chat.im.helper.SpHelper;

import io.rong.imlib.RongIMClient;

/**
 * 欢迎页
 */

public class LauncherActivity extends Activity {

    private final String TAG = "[LauncherActivity] ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher);

        String loginToken = SpHelper.getInstance().get(Constants.SP_LOGIN_TOKEN, "");
        Handler handler = new Handler();

        if (TextUtils.isEmpty(loginToken)) {//未登录
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToLogin();
                }
            }, 800);
        } else {//已登录,RongIM.connect
            RongIMClient.connect(loginToken, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    LogHelper.e(TAG + "RongIMClient.connect onTokenIncorrect");
                }

                @Override
                public void onSuccess(String loginID) {
                    LogHelper.e(TAG + "RongIMClient.connect onSuccess userID--->>" + loginID);
                    SpHelper.getInstance().put(Constants.SP_LOGIN_USERID, loginID);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogHelper.e(TAG + "RongIMClient.connect onError--->>" + errorCode.getMessage());
                }
            });

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToMain();
                }
            }, 800);
        }
    }

    //打开主界面
    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    //打开登录界面
    private void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}

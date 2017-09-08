package com.chat.im.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chat.im.R;
import com.chat.im.constant.Constants;
import com.chat.im.helper.ContextHelper;
import com.chat.im.helper.DBHelper;
import com.chat.im.helper.SpHelper;
import com.chat.im.ui.LoginActivity;

import java.util.HashMap;
import java.util.Map;

import io.rong.imlib.RongIMClient;

/**
 * 设置
 */

public class MeFragment_SettingFragment extends Fragment implements View.OnClickListener {

    private View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView(inflater);
        return mView;
    }

    private void initView(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_me_setting, null);
        mView.findViewById(R.id.exit_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit_login:
                logout();
                break;
        }
    }

    //退出登录
    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("退出登录");
        builder.setMessage("您确定要退出登录吗?");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //将当前用户的手机号 密码 id清空
                Map<String, String> map = new HashMap<>();
                map.put(Constants.SP_LOGIN_USERID, "");
                map.put(Constants.SP_LOGIN_TOKEN, "");
                map.put(Constants.SP_LOGIN_PHONE, "");
                map.put(Constants.SP_LOGIN_PASSWORD, "");
                map.put(Constants.SP_LOGIN_NICKNAME, "");
                SpHelper.getInstance().put(map);
                //关闭数据库
                DBHelper.getInstance().closeDB();
                //调用退出接口
                RongIMClient.getInstance().logout();
                //打开登录界面
                Intent loginActivityIntent = new Intent();
                loginActivityIntent.setClass(ContextHelper.getContext(), LoginActivity.class);
                loginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                ContextHelper.getContext().startActivity(loginActivityIntent);
            }
        });
        builder.show();
    }
}

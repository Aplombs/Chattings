package com.chat.im.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.constant.Constants;
import com.chat.im.helper.SpHelper;
import com.chat.im.helper.UtilsHelper;

/**
 * 我页签
 */

public class MeFragment extends Fragment implements View.OnClickListener {

    private View mView, newTip;
    private ImageView mUserHead;
    private TextView mUserNickName, mUserPhone, mFriendNotRead;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView(inflater);
        return mView;
    }

    private void initView(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_me, null);
        mUserHead = (ImageView) mView.findViewById(R.id.userHead_me_tab);
        mUserNickName = (TextView) mView.findViewById(R.id.userNickName_me_tab);
        mUserPhone = (TextView) mView.findViewById(R.id.userPhone_me_tab);
        mFriendNotRead = (TextView) mView.findViewById(R.id.not_read_friend_me_tab);
        newTip = mView.findViewById(R.id.new_tip_me_tab);

        mView.findViewById(R.id.head_nickname_qrCode_me_tab).setOnClickListener(this);
        mView.findViewById(R.id.qrCode_me_tab).setOnClickListener(this);
        mView.findViewById(R.id.wallet_me_tab).setOnClickListener(this);
        mView.findViewById(R.id.friend_me_tab).setOnClickListener(this);
        mView.findViewById(R.id.collection_me_tab).setOnClickListener(this);
        mView.findViewById(R.id.expression_me_tab).setOnClickListener(this);
        mView.findViewById(R.id.setting_me_tab).setOnClickListener(this);

        String name = SpHelper.getInstance().get(Constants.SP_LOGIN_NICKNAME, "");
        String phone = SpHelper.getInstance().get(Constants.SP_LOGIN_PHONE, "");

        mUserNickName.setText(name);
        phone = UtilsHelper.getInstance().formatPhone(phone);
        mUserPhone.setText(String.format("手机号:%s", phone));
    }

    @Override
    public void onClick(View v) {

    }
}

package com.chat.im.fragment;

import android.content.Intent;
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
import com.chat.im.helper.ContextHelper;
import com.chat.im.helper.SpHelper;
import com.chat.im.helper.UIHelper;
import com.chat.im.helper.UtilsHelper;
import com.chat.im.test.TestNewsActivity;
import com.chat.im.ui.MeActivity;

/**
 * 我页签
 */

public class MainTab_MeFragment extends Fragment implements View.OnClickListener {

    /**
     * 查看个人信息请求码
     */
    public static int USER_INFO_ME_FRAGMENT_REQUEST_CODE = 0;

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
        mView = inflater.inflate(R.layout.fragment_tab_me, null);
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
        Intent intent = new Intent(ContextHelper.getContext(), MeActivity.class);
        String tag = "";
        switch (v.getId()) {
            //个人信息
            case R.id.head_nickname_qrCode_me_tab:
                tag = Constants.TAG_USER_INFO_ME_TAB;
                intent.putExtra("tag", tag);
                startActivityForResult(intent, USER_INFO_ME_FRAGMENT_REQUEST_CODE);
                return;
            //break;
            // 个人二维码
            case R.id.qrCode_me_tab:
                UIHelper.getInstance().toast("个人二维码");
                return;
            //钱包
            case R.id.wallet_me_tab:
                tag = Constants.TAG_WALLET_ME_TAB;
                break;
            //朋友圈
            case R.id.friend_me_tab:
                startActivity(new Intent(getContext(), TestNewsActivity.class));
                //break;
                return;
            //收藏
            case R.id.collection_me_tab:
                tag = Constants.TAG_COLLECTION_ME_TAB;
                break;
            //表情
            case R.id.expression_me_tab:
                tag = Constants.TAG_EXPRESSION_ME_TAB;
                break;
            //设置
            case R.id.setting_me_tab:
                tag = Constants.TAG_SETTING_ME_TAB;
                break;
            default:
                break;
        }
        intent.putExtra("tag", tag);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == USER_INFO_ME_FRAGMENT_REQUEST_CODE) {
            //刷新个人信息UI,昵称
        }
    }
}

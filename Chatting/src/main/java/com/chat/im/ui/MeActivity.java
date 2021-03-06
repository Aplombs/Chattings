package com.chat.im.ui;

import android.support.v4.app.Fragment;
import android.view.View;

import com.chat.im.R;
import com.chat.im.constant.Constants;
import com.chat.im.fragment.MeFragment_CollectionFragment;
import com.chat.im.fragment.MeFragment_ExpressionFragment;
import com.chat.im.fragment.MeFragment_FriendFragment;
import com.chat.im.fragment.MeFragment_SettingFragment;
import com.chat.im.fragment.MeFragment_UserInfoFragment;
import com.chat.im.fragment.MeFragment_WalletFragment;

/**
 * 我的页签-点击钱包,朋友圈,表情等打开的界面
 */

public class MeActivity extends BaseActivity {

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_me;
    }

    @Override
    protected void init() {
        mBt_Add.setVisibility(View.GONE);
        mReturnView.setVisibility(View.VISIBLE);

        String tag = getIntent().getStringExtra("tag");
        mTitleName.setText(tag);

        Fragment fragment = null;
        switch (tag) {
            case Constants.TAG_USER_INFO_ME_TAB://个人信息
                fragment = new MeFragment_UserInfoFragment();
                break;
            case Constants.TAG_WALLET_ME_TAB://钱包
                fragment = new MeFragment_WalletFragment();
                break;
            case Constants.TAG_FRIEND_INFO_ME_TAB://朋友圈
                fragment = new MeFragment_FriendFragment();
                break;
            case Constants.TAG_COLLECTION_ME_TAB://收藏
                fragment = new MeFragment_CollectionFragment();
                break;
            case Constants.TAG_EXPRESSION_ME_TAB://表情
                fragment = new MeFragment_ExpressionFragment();
                break;
            case Constants.TAG_SETTING_ME_TAB://设置
                fragment = new MeFragment_SettingFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.me_activity_content, fragment).commit();
    }
}

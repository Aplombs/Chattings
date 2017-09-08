package com.chat.im.ui;

import android.support.v4.app.Fragment;
import android.view.View;

import com.chat.im.R;
import com.chat.im.constant.Constants;
import com.chat.im.fragment.PopupWindow_AddFriendFragment;
import com.chat.im.fragment.PopupWindow_CollectPaymentFragment;
import com.chat.im.fragment.PopupWindow_InitiateGroupChatFragment;
import com.chat.im.fragment.PopupWindow_ScannerFragment;

/**
 * popupWindow界面
 */

public class PopupWindowActivity extends BaseActivity {

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_popup_window;
    }

    @Override
    protected void init() {
        mBt_Add.setVisibility(View.GONE);
        mReturnView.setVisibility(View.VISIBLE);

        String tag = getIntent().getStringExtra("tag");
        mTitleName.setText(tag);

        Fragment fragment = null;
        switch (tag) {
            case Constants.TAG_INITIATE_GROUP_CHAT_POPUP_WINDOW://发起群聊
                fragment = new PopupWindow_InitiateGroupChatFragment();
                break;
            case Constants.TAG_ADD_FRIEND_POPUP_WINDOW://添加好友
                fragment = new PopupWindow_AddFriendFragment();
                break;
            case Constants.TAG_SCANNER_POPUP_WINDOW://扫一扫
                fragment = new PopupWindow_ScannerFragment();
                break;
            case Constants.TAG_COLLECT_PAYMENT_POPUP_WINDOW://收付款
                fragment = new PopupWindow_CollectPaymentFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.popup_window_content, fragment).commit();
    }
}

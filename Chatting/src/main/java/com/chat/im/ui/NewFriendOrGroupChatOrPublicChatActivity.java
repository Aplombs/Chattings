package com.chat.im.ui;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import com.chat.im.R;
import com.chat.im.constant.Constants;
import com.chat.im.fragment.ContactFragment_GroupChatFragment;
import com.chat.im.fragment.ContactFragment_NewFriendFragment;
import com.chat.im.fragment.ContactFragment_PublicChatFragment;

/**
 * 新朋友 群聊 公众号界面
 */

public class NewFriendOrGroupChatOrPublicChatActivity extends BaseActivity {

    private OnBackListener onBackListener;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_newfriend_group_public_chat;
    }

    @Override
    protected void init() {
        mBt_Add.setVisibility(View.GONE);
        mReturnView.setVisibility(View.VISIBLE);

        String tag = getIntent().getStringExtra("tag");
        mTitleName.setText(tag);

        Fragment fragment = null;
        switch (tag) {
            case Constants.TAG_NEW_FRIEND_HEADER://新朋友
                fragment = new ContactFragment_NewFriendFragment();
                break;
            case Constants.TAG_GROUP_CHAT_HEADER://群聊
                fragment = new ContactFragment_GroupChatFragment();
                break;
            case Constants.TAG_PUBLIC_CHAT_HEADER://公众号
                fragment = new ContactFragment_PublicChatFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.new_friend_group_public_activity_content, fragment).commit();

        mReturnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBackListener != null) {
                    onBackListener.onBack();
                } else {
                    NewFriendOrGroupChatOrPublicChatActivity.this.finish();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onBackListener != null) {
                onBackListener.onBack();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setOnBackListener(OnBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    public interface OnBackListener {
        void onBack();
    }
}

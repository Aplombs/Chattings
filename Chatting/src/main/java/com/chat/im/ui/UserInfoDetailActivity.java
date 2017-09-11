package com.chat.im.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.constant.Constants;
import com.chat.im.db.bean.ContactInfo;
import com.chat.im.db.dao.ContactInfoDao;
import com.chat.im.helper.DBHelper;
import com.chat.im.helper.UtilsHelper;

/**
 * 用户详细资料界面
 */

public class UserInfoDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mUserHead;
    private TextView mUserRemarkName, mUserPhone, mUserNickName;
    private String userID, phone, nickName, userHeadUri;
    private View sendMessage;
    private View requestAddFriend;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_user_info_detail;
    }

    @Override
    protected void init() {
        mTitleName.setText("详细资料");
        mBt_Add.setVisibility(View.GONE);
        mReturnView.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        userID = intent.getStringExtra(Constants.USER_ID);
        phone = intent.getStringExtra(Constants.USER_PHONE);
        nickName = intent.getStringExtra(Constants.USER_NICK_NAME);
        userHeadUri = intent.getStringExtra(Constants.USER_HEAD_URI);

        String remarkName = "";
        ContactInfo contactInfo = DBHelper.getInstance().getDaoSession().getContactInfoDao().queryBuilder().where(ContactInfoDao.Properties.UserId.eq(userID)).unique();
        if (contactInfo != null) {
            remarkName = contactInfo.getRemarkName();
        }

        mUserHead = (ImageView) findViewById(R.id.userHead_user_detail);
        mUserRemarkName = (TextView) findViewById(R.id.userRemarkName_user_detail);
        mUserNickName = (TextView) findViewById(R.id.userNickName_user_detail);
        mUserPhone = (TextView) findViewById(R.id.userPhone_user_detail);
        sendMessage = findViewById(R.id.send_message_user_detail);
        requestAddFriend = findViewById(R.id.request_add_friend_user_detail);

        if (!TextUtils.isEmpty(remarkName)) {
            mUserRemarkName.setVisibility(View.VISIBLE);
            mUserRemarkName.setText(remarkName);
        } else {
            mUserRemarkName.setVisibility(View.GONE);
        }

        mUserNickName.setText(String.format("昵称:%s", nickName));
        mUserPhone.setText(UtilsHelper.getInstance().formatPhone(phone));

        if (contactInfo != null) {
            sendMessage.setVisibility(View.VISIBLE);
            sendMessage.setOnClickListener(this);
            requestAddFriend.setVisibility(View.GONE);
        } else {
            sendMessage.setVisibility(View.GONE);
            requestAddFriend.setVisibility(View.VISIBLE);
            requestAddFriend.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_message_user_detail:
                break;
            case R.id.request_add_friend_user_detail:
                break;

        }
    }
}
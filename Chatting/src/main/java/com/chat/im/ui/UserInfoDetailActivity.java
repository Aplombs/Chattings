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
    private String userID, fromWhereFlag;
    private View sendMessage;
    private View requestAddFriend;
    private Intent mIntent;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_user_info_detail;
    }

    @Override
    protected void init() {
        mTitleName.setText("详细资料");
        mBt_Add.setVisibility(View.GONE);
        mReturnView.setVisibility(View.VISIBLE);

        mIntent = getIntent();
        userID = mIntent.getStringExtra(Constants.USER_ID);

        mUserHead = (ImageView) findViewById(R.id.userHead_user_detail);
        mUserRemarkName = (TextView) findViewById(R.id.userRemarkName_user_detail);
        mUserNickName = (TextView) findViewById(R.id.userNickName_user_detail);
        mUserPhone = (TextView) findViewById(R.id.userPhone_user_detail);
        sendMessage = findViewById(R.id.send_message_user_detail);
        requestAddFriend = findViewById(R.id.request_add_friend_user_detail);

        ContactInfo contactInfo = DBHelper.getInstance().getDaoSession().getContactInfoDao().queryBuilder().where(ContactInfoDao.Properties.UserId.eq(userID)).unique();

        if (contactInfo != null) {
            isMyContact(contactInfo);
        } else {
            isNotContact();
        }
    }

    private void isMyContact(ContactInfo contactInfo) {
        String remarkName = contactInfo.getRemarkName();
        String nickName = contactInfo.getNickName();
        String phone = contactInfo.getPhone();

        //优先展示备注 没有备注展示昵称;有备注的话备注名在第一行展示,昵称在第二行
        if (!TextUtils.isEmpty(remarkName)) {
            mUserRemarkName.setText(remarkName);
            mUserNickName.setVisibility(View.VISIBLE);
            mUserNickName.setText(String.format("昵称:%s", nickName));
        } else {//没有备注的话隐藏展示昵称的控件
            mUserNickName.setVisibility(View.GONE);
            mUserRemarkName.setText(nickName);
        }

        mUserPhone.setText(UtilsHelper.getInstance().formatPhone(phone));

        sendMessage.setVisibility(View.VISIBLE);
        sendMessage.setOnClickListener(this);
        requestAddFriend.setVisibility(View.GONE);
    }

    private void isNotContact() {
        String phone = mIntent.getStringExtra(Constants.USER_PHONE);
        String nickName = mIntent.getStringExtra(Constants.USER_NICK_NAME);
        String userHeadUri = mIntent.getStringExtra(Constants.USER_HEAD_URI);

        mUserNickName.setVisibility(View.GONE);
        mUserRemarkName.setText(nickName);

        mUserPhone.setText(UtilsHelper.getInstance().formatPhone(phone));

        sendMessage.setVisibility(View.GONE);
        requestAddFriend.setOnClickListener(this);
        requestAddFriend.setVisibility(View.VISIBLE);
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
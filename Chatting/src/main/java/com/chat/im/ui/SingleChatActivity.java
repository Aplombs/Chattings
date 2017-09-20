package com.chat.im.ui;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chat.im.R;
import com.chat.im.constant.Constants;
import com.chat.im.db.bean.ContactInfo;
import com.chat.im.db.dao.ContactInfoDao;
import com.chat.im.helper.DBHelper;

/**
 * 单聊界面
 */

public class SingleChatActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private ContactInfo mContactInfo;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_single_chat;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        String userId = intent.getStringExtra(Constants.USER_ID);
        mContactInfo = DBHelper.getInstance().getDaoSession().getContactInfoDao().queryBuilder().where(ContactInfoDao.Properties.UserId.eq(userId)).unique();

        mTitleName.setText(mContactInfo.getShowName());
        mBt_Add.setVisibility(View.GONE);
        mReturnView.setVisibility(View.VISIBLE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_single_chat);
    }
}
package com.chat.im.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chat.im.R;
import com.chat.im.adapter.ChattingAdapter;
import com.chat.im.constant.Constants;
import com.chat.im.db.bean.ContactInfo;
import com.chat.im.db.bean.message.MessageBase;
import com.chat.im.db.dao.ContactInfoDao;
import com.chat.im.helper.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 单聊界面
 */

public class SingleChatActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private ContactInfo mContactInfo;
    private ChattingAdapter mAdapter;

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
        List<MessageBase> mList = new ArrayList<>();
        MessageBase messageBase = new MessageBase();
        messageBase.setMessageContentType(Constants.MESSAGE_CONTENTTYPE_TEXT);
        messageBase.setMessageDirection(1);

        MessageBase messageBase2 = new MessageBase();
        messageBase2.setMessageContentType(Constants.MESSAGE_CONTENTTYPE_TEXT);
        messageBase2.setMessageDirection(2);

        mList.add(messageBase);
        mList.add(messageBase2);

        mList.add(messageBase);
        mList.add(messageBase2);

        mAdapter = new ChattingAdapter(this, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }
}
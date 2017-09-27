package com.chat.im.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.chat.im.R;
import com.chat.im.adapter.ChattingAdapter;
import com.chat.im.constant.Constants;
import com.chat.im.db.bean.ContactInfo;
import com.chat.im.db.bean.message.MessageBase;
import com.chat.im.db.dao.ContactInfoDao;
import com.chat.im.helper.DBHelper;
import com.chat.im.helper.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 单聊界面
 */

public class SingleChatActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private ContactInfo mContactInfo;
    private ChattingAdapter mAdapter;
    private List<MessageBase> mList;
    private EditText mEditContent;
    private Handler mHandler = new Handler();

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
        mEditContent = (EditText) findViewById(R.id.edit_content_single_chat);

        mList = new ArrayList<>();
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

        mList.add(messageBase);
        mList.add(messageBase2);

        mList.add(messageBase);
        mList.add(messageBase2);


        mList.add(messageBase);
        mList.add(messageBase2);

        mList.add(messageBase);
        mList.add(messageBase2);

        mList.add(messageBase);
        mList.add(messageBase2);

        mAdapter = new ChattingAdapter(this, mList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);//软键盘弹出 布局展示最后一个item
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mEditContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.scrollToPosition(mList.size() - 1);
    }

    public void showSoftInput() {
        //输入法弹出,输入框显示光标
        UIHelper.getInstance().showSoftInput(mEditContent);
    }

    private void hideSoftInput() {
        //输入法消失,输入框隐藏光标
        UIHelper.getInstance().hideSoftInput(mEditContent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
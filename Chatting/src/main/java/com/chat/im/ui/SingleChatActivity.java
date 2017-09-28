package com.chat.im.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
import java.util.Random;

/**
 * 单聊界面
 */

public class SingleChatActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private ContactInfo mContactInfo;
    private ChattingAdapter mAdapter;
    private List<MessageBase> mList = new ArrayList<>();
    private EditText mEditContent;
    private Handler mHandler = new Handler();
    private ImageView mPlayVoice, mAddMore;
    private Button mSendContent;
    private boolean isSendOrReceive;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_single_chat;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        String userId = intent.getStringExtra(Constants.USER_ID);
        mContactInfo = DBHelper.getInstance().getDaoSession().getContactInfoDao().queryBuilder().where(ContactInfoDao.Properties.UserId.eq(userId)).unique();

        initList();

        mTitleName.setText(mContactInfo.getShowName());
        mBt_Add.setVisibility(View.GONE);
        mReturnView.setVisibility(View.VISIBLE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_single_chat);
        mPlayVoice = (ImageView) findViewById(R.id.play_voice_single_chat);
        mEditContent = (EditText) findViewById(R.id.edit_content_single_chat);
        mAddMore = (ImageView) findViewById(R.id.more_single_chat);
        mSendContent = (Button) findViewById(R.id.send_content_single_chat);

        mSendContent.setOnClickListener(this);

        mAdapter = new ChattingAdapter(this, mList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);//软键盘弹出 布局展示最后一个item
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mEditContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.toString().length();
                if (length > 0) {
                    mAddMore.setVisibility(View.GONE);
                    mSendContent.setVisibility(View.VISIBLE);
                } else {
                    mAddMore.setVisibility(View.VISIBLE);
                    mSendContent.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initList() {
        mList = DBHelper.getInstance().getDaoSession().getMessageBaseDao().queryBuilder().list();
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
            case R.id.send_content_single_chat:
                sendMessage();
                break;
        }
    }

    //发送消息
    private void sendMessage() {
        String message = mEditContent.getText().toString();
        mEditContent.setText("");
        MessageBase messageBase = new MessageBase();
        messageBase.setMessageId(String.valueOf(new Random().nextInt()));
        messageBase.setMessageContent(message);
        if (isSendOrReceive) {
            messageBase.setMessageContentType(Constants.MESSAGE_CONTENT_TYPE_TEXT_SEND);
        } else {
            messageBase.setMessageContentType(Constants.MESSAGE_CONTENT_TYPE_TEXT_RECEIVE);
        }

        isSendOrReceive = !isSendOrReceive;
        mList.add(messageBase);
        mAdapter.notifyItemInserted(mList.size() - 1);
        mRecyclerView.scrollToPosition(mList.size() - 1);

        DBHelper.getInstance().getDaoSession().getMessageBaseDao().insert(messageBase);
    }
}
package com.chat.im.ui;

import android.content.Intent;
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
import com.chat.im.db.bean.MessagePreView;
import com.chat.im.db.bean.message.MessageBase;
import com.chat.im.helper.DBHelper;
import com.chat.im.helper.LogHelper;
import com.chat.im.helper.UIHelper;
import com.chat.im.notify.NotifyHelper;
import com.chat.im.notify.NotifyReceiver;
import com.chat.im.view.ResizeRelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 单聊界面
 */

public class SingleChatActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "[SingleChatActivity] ";
    private Button mSendContent;
    private EditText mEditContent;
    private int mMenuOpenedHeight;
    private boolean isSendOrReceive;
    //是否发送过消息
    private boolean isSendedMessage;
    private ContactInfo mContactInfo;
    private ChattingAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ImageView mPlayVoice, mAddMore;
    private List<MessageBase> mList = new ArrayList<>();

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_single_chat;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        String userId = intent.getStringExtra(Constants.USER_ID);
        mContactInfo = DBHelper.getInstance().getContactDao().queryContact(userId);

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
        //reverseLayout false:新添加的在最下面 true:新添加的在最上面
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //linearLayoutManager.setStackFromEnd(true);//软键盘弹出 将最后item被顶上去
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

        mReturnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealBackPress();
            }
        });

        ResizeRelativeLayout resizeRelativeLayout = (ResizeRelativeLayout) findViewById(R.id.single_chat_root);
        //监听软键盘的变化
        resizeRelativeLayout.setOnResizeRelativeListener(new ResizeRelativeLayout.OnResizeRelativeListener() {
            @Override
            public void onResizeRelative(int with, int height, int oldWith, int oldHeight) {
                boolean isKeyboardOpened = false;

                // 记录第一次打开输入法时的布局高度
                if (height < oldWith && oldWith > 0 && mMenuOpenedHeight == 0) {
                    mMenuOpenedHeight = height;
                }

                //布局当前的高度小于之前的高度
                if (height < oldHeight) {
                    isKeyboardOpened = true;
                }// 或者输入法打开情况下,输入字符后再清除(三星输入法软键盘在输入后,软键盘高度增加一行,清除输入后,高度变小,但是软键盘仍是打开状态)
                else if ((height <= mMenuOpenedHeight) && (mMenuOpenedHeight != 0)) {
                    isKeyboardOpened = true;
                }

                if (isKeyboardOpened) {
                    LogHelper.e(TAG + "软键盘弹起 height:" + height + "  oldHeight:" + oldHeight);
                    mRecyclerView.scrollToPosition(mList.size() - 1);
                } else {
                    LogHelper.e(TAG + "软键盘隐藏 height:" + height + "  oldHeight:" + oldHeight);
                }
            }
        });
    }

    private void initList() {
        mList = DBHelper.getInstance().getMessageBaseDao().queryAllMessages(mContactInfo.getUserId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.scrollToPosition(mList.size() - 1);
    }

    @Override
    public void onBackPressed() {
        dealBackPress();
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
            default:
                break;
        }
    }

    //处理返回键
    private void dealBackPress() {
        //是否发送过消息 是的话返回直接到消息页签
        if (isSendedMessage && null != mList && mList.size() > 0) {
            NotifyHelper.getInstance().notifyEvent(NotifyReceiver.NOTIFY_TYPE_UPDATE_MESSAGE_PREVIEW, null);
        }
        this.finish();
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

        messageBase.setMessageTo(mContactInfo.getUserId());

        isSendOrReceive = !isSendOrReceive;
        mList.add(messageBase);
        mRecyclerView.scrollToPosition(mList.size() - 1);

        DBHelper.getInstance().getMessageBaseDao().insertMessage(messageBase);

        //生成一条预览消息
        MessagePreView messagePreView = new MessagePreView(mContactInfo.getUserId(), mContactInfo.getShowName(), messageBase.getMessageContent(), mList.size() + "", false);
        //如果消息存在就更新
        if (DBHelper.getInstance().getMessagePreViewDao().queryMessagePreViewIsExist(messagePreView.getMessagePreviewId())) {
            //更新预览消息
            DBHelper.getInstance().getMessagePreViewDao().updateMessagePreView(messagePreView);
        } else {
            DBHelper.getInstance().getMessagePreViewDao().insertMessagePreView(messagePreView);
        }
        NotifyHelper.getInstance().notifyEvent(NotifyReceiver.NOTIFY_TYPE_UPDATE_MESSAGE_PREVIEW, null);

        isSendedMessage = true;
    }
}
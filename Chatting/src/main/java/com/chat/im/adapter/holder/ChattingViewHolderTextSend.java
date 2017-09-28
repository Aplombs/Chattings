package com.chat.im.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.db.bean.message.MessageBase;

/**
 * 文本--发送方
 */

public class ChattingViewHolderTextSend extends ChattingViewHolder {

    private TextView mContentSend;

    public ChattingViewHolderTextSend(View itemView) {
        super(itemView);
        mContentSend = (TextView) mView.findViewById(R.id.text_content_send);
    }

    @Override
    public void initView(MessageBase messageBase) {
        mContentSend.setText(messageBase.getMessageContent());
    }
}

package com.chat.im.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.db.bean.message.MessageBase;

/**
 * 文本--接收方
 */

public class ChattingViewHolderTextReceive extends ChattingViewHolder {

    private TextView mContentReceive;

    public ChattingViewHolderTextReceive(View itemView) {
        super(itemView);
        mContentReceive = (TextView) mView.findViewById(R.id.text_content_receive);
    }

    @Override
    public void initView(MessageBase messageBase) {
        mContentReceive.setText(messageBase.getMessageContent());
    }
}

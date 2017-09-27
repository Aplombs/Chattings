package com.chat.im.adapter.holder;

import android.view.View;

import com.chat.im.R;
import com.chat.im.db.bean.message.MessageBase;

/**
 * 文本
 */

public class ChattingViewHolderText extends ChattingViewHolder {

    public ChattingViewHolderText(View itemView) {
        super(itemView);
    }

    @Override
    public void initView(MessageBase messageBase) {
        int messageDirection = messageBase.getMessageDirection();

        View messageSend = mView.findViewById(R.id.message_text_send);
        View messageReceive = mView.findViewById(R.id.message_text_receive);

        if (1 == messageDirection) {
            messageReceive.setVisibility(View.GONE);
            messageSend.setVisibility(View.VISIBLE);
        } else {
            messageReceive.setVisibility(View.VISIBLE);
            messageSend.setVisibility(View.GONE);
        }
    }
}

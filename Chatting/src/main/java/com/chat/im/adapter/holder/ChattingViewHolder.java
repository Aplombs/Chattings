package com.chat.im.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chat.im.db.bean.message.MessageBase;

/**
 * 不同消息展示布局的holder基类
 */

public class ChattingViewHolder extends RecyclerView.ViewHolder {

    protected View mView;

    public ChattingViewHolder(View itemView) {
        super(itemView);
        this.mView = itemView;
    }

    public void initView(MessageBase messageBase) {
    }
}
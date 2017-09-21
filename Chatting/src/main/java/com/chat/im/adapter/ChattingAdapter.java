package com.chat.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chat.im.R;
import com.chat.im.adapter.holder.ChattingViewHolder;
import com.chat.im.adapter.holder.ChattingViewHolderText;
import com.chat.im.adapter.holder.ChattingViewHolderUnKnown;
import com.chat.im.constant.Constants;
import com.chat.im.db.bean.message.MessageBase;

import java.util.List;

/**
 * 聊天界面RecyclerView的Adapter
 */

public class ChattingAdapter extends RecyclerView.Adapter<ChattingViewHolder> {

    private int position;
    private Context mContext;
    private List<MessageBase> mList;

    public ChattingAdapter(Context context, List<MessageBase> messageBaseList) {
        this.mContext = context;
        this.mList = messageBaseList;
    }

    public void reloadList(List<MessageBase> messageBaseList) {
        if (mList != null) {
            this.mList.clear();
        }
        this.mList = messageBaseList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        this.position = position;
        return mList.get(position).getMessageContentType();
    }

    private MessageBase getItemData() {
        return mList.get(position);
    }

    @Override
    public ChattingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createHolder();
    }

    @Override
    public void onBindViewHolder(ChattingViewHolder holder, final int position) {
        holder.initView();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private ChattingViewHolder createHolder() {
        MessageBase messageBase = getItemData();
        int messageContentType = messageBase.getMessageContentType();
        int direction = messageBase.getMessageDirection();
        ChattingViewHolder viewHolder;
        if (messageContentType == Constants.MESSAGE_CONTENTTYPE_TEXT) {
            View view;
            if (direction == 1) {//发送
                view = View.inflate(mContext, R.layout.itemview_message_text_send, null);
            } else {//接收
                view = View.inflate(mContext, R.layout.itemview_message_text_receive, null);
            }
            viewHolder = new ChattingViewHolderText(view);
        } else {
            viewHolder = new ChattingViewHolderUnKnown(null);
        }
        return viewHolder;
    }

    private class ViewType {
        private static final int MODE_SHIFT = 30;
        private static final int MODE_MASK = 0x3 << MODE_SHIFT;

        public int makeViewType(int size, int mode) {

            return size + mode;
        }

        public int getViewType(int viewType) {
            return (viewType & MODE_MASK);
        }

        public int getPosition(int viewType) {
            return (viewType & ~MODE_MASK);
        }
    }
}
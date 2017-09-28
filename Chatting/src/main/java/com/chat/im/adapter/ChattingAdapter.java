package com.chat.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
        return mList.get(position).getMessageContentType();
    }

    @Override
    public ChattingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ChattingViewHolder holder, final int position) {
        MessageBase messageBase = mList.get(position);
        holder.initView(messageBase);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private ChattingViewHolder createHolder(ViewGroup parent, int viewType) {
        ChattingViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == Constants.MESSAGE_CONTENTTYPE_TEXT) {
            View view = inflater.inflate(R.layout.itemview_message_text, parent, false);
            View.inflate(mContext,R.layout.itemview_message_unknown,null);
            viewHolder = new ChattingViewHolderText(view);
        } else {
            View view = inflater.inflate(R.layout.itemview_message_unknown, parent, false);
            viewHolder = new ChattingViewHolderUnKnown(view);
        }
        return viewHolder;
    }
}
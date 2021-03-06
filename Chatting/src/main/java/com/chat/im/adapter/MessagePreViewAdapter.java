package com.chat.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.db.bean.MessagePreView;
import com.chat.im.helper.ContextHelper;

import java.util.List;

/**
 * 消息页签--预览消息RecyclerView的Adapter
 */

public class MessagePreViewAdapter extends RecyclerView.Adapter<MessagePreViewAdapter.MessagePreViewHolder> {

    private Context mContext;
    private List<MessagePreView> mList;
    private onItemClickListener itemClickListener;

    public MessagePreViewAdapter(Context context, List<MessagePreView> messagePreViewList) {
        this.mContext = context;
        this.mList = messagePreViewList;
    }

    public void reloadList(List<MessagePreView> messagePreViewList) {
        if (mList != null) {
            this.mList.clear();
        }
        this.mList = messagePreViewList;
    }

    @Override
    public MessagePreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(ContextHelper.getContext(), R.layout.itemview_tab_message, null);
        return new MessagePreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessagePreViewHolder holder, final int position) {
        final MessagePreView messagePreView = mList.get(position);
        if (messagePreView.getIsTop()) {
            holder.itemView.setBackgroundColor(ContextHelper.getContext().getResources().getColor(R.color.message_to_top));
        } else {
            holder.itemView.setBackgroundColor(ContextHelper.getContext().getResources().getColor(R.color.white));
        }
        holder.userName.setText(messagePreView.getUserNickName());
        holder.contentPreView.setText(messagePreView.getContentPreView());
        String notReadMessageNum = messagePreView.getNotReadMessageNum();
        if (!"0".equals(notReadMessageNum)) {
            holder.not_read_message.setText(notReadMessageNum);
        } else {
            holder.not_read_message.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setItemClick(onItemClickListener itemClick) {
        this.itemClickListener = itemClick;
    }

    public class MessagePreViewHolder extends RecyclerView.ViewHolder {

        public ImageView userHead;
        public TextView userName, contentPreView, not_read_message;

        public MessagePreViewHolder(View itemView) {
            super(itemView);
            userHead = (ImageView) itemView.findViewById(R.id.userHead_MessagePreView);
            userName = (TextView) itemView.findViewById(R.id.userNickName_MessagePreView);
            contentPreView = (TextView) itemView.findViewById(R.id.contentPreview_MessagePreView);
            not_read_message = (TextView) itemView.findViewById(R.id.not_read_message_MessagePreView);
        }
    }
}

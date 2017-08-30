package com.chat.im.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.db.bean.MessagePreView;
import com.chat.im.helper.ContextHelper;

import java.util.List;
import java.util.Random;

/**
 * 消息页签--预览消息RecyclerView的Adapter
 */

public class MessagePreViewAdapter extends RecyclerView.Adapter<MessagePreViewAdapter.MessagePreViewHolder> {

    private List<MessagePreView> mList;

    public MessagePreViewAdapter(List<MessagePreView> messagePreViewList) {
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
    public void onBindViewHolder(MessagePreViewHolder holder, int position) {
        MessagePreView messagePreView = mList.get(position);
        if (messagePreView.getIsTop()) {
            holder.itemView.setBackgroundColor(ContextHelper.getContext().getResources().getColor(R.color.common_bg));
        } else {
            holder.itemView.setBackgroundColor(ContextHelper.getContext().getResources().getColor(R.color.white));
        }
        holder.userName.setText(messagePreView.getUserNickName());
        holder.contentPreView.setText(messagePreView.getContentPreView());
        holder.not_read_message.setText(String.valueOf(new Random().nextInt(10)));
    }

    @Override
    public int getItemCount() {
        return mList.size();
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

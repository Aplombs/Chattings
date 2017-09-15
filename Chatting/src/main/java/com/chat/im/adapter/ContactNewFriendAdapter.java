package com.chat.im.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.db.bean.WaitAddFriends;
import com.chat.im.helper.ContextHelper;

import java.util.List;

/**
 * 联系人页签--新朋友RecyclerView的Adapter
 */

public class ContactNewFriendAdapter extends RecyclerView.Adapter<ContactNewFriendAdapter.ContactNewFriendViewHolder> {

    private List<WaitAddFriends> mList;
    private AgreeItemClickListener mItemAgreeListener;

    public ContactNewFriendAdapter(List<WaitAddFriends> waitAddFriendsList) {
        this.mList = waitAddFriendsList;
    }

    public void reloadList(List<WaitAddFriends> waitAddFriendsList) {
        if (mList != null) {
            this.mList.clear();
        }
        this.mList = waitAddFriendsList;
        notifyDataSetChanged();
    }

    @Override
    public ContactNewFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(ContextHelper.getContext(), R.layout.itemview_contact_new_friend, null);
        return new ContactNewFriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactNewFriendViewHolder holder, final int position) {
        WaitAddFriends waitAddFriends = mList.get(position);

        holder.userName.setText(waitAddFriends.getNickName());
        holder.attachMessage.setText(waitAddFriends.getAddFriendAttachMessage());

        if (waitAddFriends.getIsAdded()) {
            holder.agree_add_friend.setEnabled(false);
            holder.agree_add_friend.setText("已添加");
            holder.agree_add_friend.setBackgroundResource(R.drawable.add_friend_yes_agree_btn_round);
        } else {
            holder.agree_add_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemAgreeListener != null) {
                        mItemAgreeListener.onItemAgreeClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnAgreeClick(AgreeItemClickListener listener) {
        this.mItemAgreeListener = listener;
    }

    public interface AgreeItemClickListener {
        void onItemAgreeClick(int position);
    }

    public class ContactNewFriendViewHolder extends RecyclerView.ViewHolder {

        public ImageView userHead;
        public Button agree_add_friend;
        public TextView userName, attachMessage;

        public ContactNewFriendViewHolder(View itemView) {
            super(itemView);
            userHead = (ImageView) itemView.findViewById(R.id.userHead_item_contact_new_friend);
            userName = (TextView) itemView.findViewById(R.id.userNickName_item_contact_new_friend);
            attachMessage = (TextView) itemView.findViewById(R.id.attach_message_item_contact_new_friend);
            agree_add_friend = (Button) itemView.findViewById(R.id.agree_add_friend);
        }
    }
}
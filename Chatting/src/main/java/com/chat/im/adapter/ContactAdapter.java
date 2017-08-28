package com.chat.im.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.db.bean.ContactInfo;
import com.chat.im.helper.ContextHelper;

import java.util.List;

/**
 * 联系人页签--联系人信息RecyclerView的Adapter
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<ContactInfo> mList;

    public ContactAdapter(List<ContactInfo> contactInfoList) {
        this.mList = contactInfoList;
    }

    public void reloadList(List<ContactInfo> contactInfoList) {
        if (mList != null) {
            this.mList.clear();
        }
        this.mList = contactInfoList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = //View.inflate(ContextHelper.getContext(), R.layout.itemview_tab_contact, null);
                LayoutInflater.from(ContextHelper.getContext()).inflate(R.layout.itemview_tab_contact, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        ContactInfo contactInfo = mList.get(position);
        holder.userName.setText(contactInfo.getUserNickName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public ImageView userHead;
        public TextView userName, firstLetter;

        public ContactViewHolder(View itemView) {
            super(itemView);
            userHead = (ImageView) itemView.findViewById(R.id.userHead_itemContact);
            userName = (TextView) itemView.findViewById(R.id.userNickName_itemContact);
            firstLetter = (TextView) itemView.findViewById(R.id.firstLetter_itemContact);
        }
    }
}
package com.chat.im.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.db.bean.ContactInfo;
import com.chat.im.helper.ContextHelper;

import java.util.List;
import java.util.Map;

/**
 * 联系人页签--联系人信息RecyclerView的Adapter
 */

public class ContactAdapter extends BaseExpandableListAdapter {

    private List<String> mLetterList;
    private Map<String, List<ContactInfo>> mMap;

    public ContactAdapter(List<String> letterList, Map<String, List<ContactInfo>> map) {
        this.mMap = map;
        this.mLetterList = letterList;
    }

    @Override
    public int getGroupCount() {
        return mMap.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mMap.get(mLetterList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ContactLetterViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ContextHelper.getContext()).inflate(R.layout.itemview_tab_contact_letter, parent, false);
            holder = new ContactLetterViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ContactLetterViewHolder) convertView.getTag();
        }

        holder.Letter.setText(mLetterList.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ContactInfoViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ContextHelper.getContext()).inflate(R.layout.itemview_tab_contact_content, parent, false);
            holder = new ContactInfoViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ContactInfoViewHolder) convertView.getTag();
        }

        holder.userName.setText(mMap.get(mLetterList.get(groupPosition)).get(childPosition).getShowName());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class ContactLetterViewHolder {

        TextView Letter;

        public ContactLetterViewHolder(View itemView) {
            Letter = (TextView) itemView.findViewById(R.id.firstLetter_itemContact);
        }
    }

    private class ContactInfoViewHolder {

        ImageView userHead;
        TextView userName;

        public ContactInfoViewHolder(View itemView) {
            userHead = (ImageView) itemView.findViewById(R.id.userHead_itemContact);
            userName = (TextView) itemView.findViewById(R.id.userNickName_itemContact);
        }
    }
}
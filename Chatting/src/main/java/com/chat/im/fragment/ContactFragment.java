package com.chat.im.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.adapter.ContactAdapter;
import com.chat.im.db.bean.ContactInfo;
import com.chat.im.db.dao.ContactInfoDao;
import com.chat.im.db.dao.DaoMaster;
import com.chat.im.db.dao.DaoSession;
import com.chat.im.helper.ContextHelper;

import java.util.List;

/**
 * 联系人页签
 */

public class ContactFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private TextView mContactNum;
    private ContactAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<ContactInfo> mContactInfoList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(ContextHelper.getContext(), "contact_info.db");
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        ContactInfoDao contactInfoDao = daoSession.getContactInfoDao();
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setUserNickName("唐利涛");
        contactInfoDao.insert(contactInfo);
        mContactInfoList = contactInfoDao.queryBuilder().list();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_contact, null);
        initView();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        mView.findViewById(R.id.newFriend_Contact).setOnClickListener(this);
        mView.findViewById(R.id.groupChat_Contact).setOnClickListener(this);
        mView.findViewById(R.id.publicChat_Contact).setOnClickListener(this);
        mContactNum = (TextView) mView.findViewById(R.id.contactNum_Contact);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView_tabContact);

        mContactNum.setText(String.format("%d位联系人", mContactInfoList.size()));

        mAdapter = new ContactAdapter(mContactInfoList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ContextHelper.getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newFriend_Contact:
                break;
            case R.id.groupChat_Contact:
                break;
            case R.id.publicChat_Contact:
                break;
        }
    }
}

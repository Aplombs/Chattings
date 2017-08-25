package com.chat.im.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chat.im.R;
import com.chat.im.adapter.MessagePreViewAdapter;
import com.chat.im.db.bean.MessagePreView;
import com.chat.im.db.dao.DaoMaster;
import com.chat.im.db.dao.DaoSession;
import com.chat.im.db.dao.MessagePreViewDao;
import com.chat.im.helper.ContextHelper;

import java.util.List;

/**
 * 消息页签
 */

public class MessageFragment extends Fragment {

    private View mView;
    private RecyclerView mRecyclerView;
    private List<MessagePreView> mMessagePreViewList;
    private MessagePreViewAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(ContextHelper.getContext(), "message_preview.db");
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        MessagePreViewDao messagePreViewDao = daoSession.getMessagePreViewDao();
        MessagePreView messagePreView = new MessagePreView();
        messagePreView.setUserNickName("习大大");
        messagePreView.setContentPreView("[语音]");
        messagePreViewDao.insert(messagePreView);
        mMessagePreViewList = messagePreViewDao.queryBuilder().list();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_message, null);
        initView();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView_tabMessage);
        mAdapter = new MessagePreViewAdapter(mMessagePreViewList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ContextHelper.getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }
}

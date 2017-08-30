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
import com.chat.im.helper.ContextHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息页签
 */

public class MessageFragment extends Fragment {

    private View mView;
    private RecyclerView mRecyclerView;
    private List<MessagePreView> mMessagePreViewList = new ArrayList<>();
    private MessagePreViewAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

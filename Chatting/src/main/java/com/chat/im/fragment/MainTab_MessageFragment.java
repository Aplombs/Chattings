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
import com.chat.im.helper.DBHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 消息页签
 */

public class MainTab_MessageFragment extends Fragment {

    private View mView;
    private RecyclerView mRecyclerView;
    private List<MessagePreView> mMessagePreViewList = new ArrayList<>();
    private MessagePreViewAdapter mAdapter;
    private View mNotMessageTip;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.create(new ObservableOnSubscribe<List<MessagePreView>>() {
            @Override
            public void subscribe(ObservableEmitter<List<MessagePreView>> observableEmitter) {
                mMessagePreViewList = DBHelper.getInstance().getMessagePreViewDao().queryAllMessagePreView();
            }
        }).subscribeOn(Schedulers.io())//被观察者在子线程
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<MessagePreView>>() {
            @Override
            public void accept(List<MessagePreView> messagePreViewList) throws Exception {
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_tab_message, null);
        initView();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMessagePreViewList.size() == 0) {
            mNotMessageTip.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNotMessageTip.setVisibility(View.GONE);
        }
    }

    private void initView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView_tabMessage);
        mNotMessageTip = mView.findViewById(R.id.ll_not_message_tip);
        mAdapter = new MessagePreViewAdapter(mMessagePreViewList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ContextHelper.getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }
}

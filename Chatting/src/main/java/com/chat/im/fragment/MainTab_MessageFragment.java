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
import com.chat.im.notify.NotifyMonitor;
import com.chat.im.notify.NotifyReceiver;

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

public class MainTab_MessageFragment extends Fragment implements NotifyMonitor.NotifyMonitorListener {

    private RecyclerView mRecyclerView;
    private MessagePreViewAdapter mAdapter;
    private View mView, mNotMessageTip, mLoading;
    private List<MessagePreView> mMessagePreViewList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotifyMonitor.getInstance().registerNotifyMonitorListener(this);
        loadMessagePreView();
    }

    private void loadMessagePreView() {
        Observable.create(new ObservableOnSubscribe<List<MessagePreView>>() {
            @Override
            public void subscribe(ObservableEmitter<List<MessagePreView>> observableEmitter) {
                List<MessagePreView> messagePreViewList = DBHelper.getInstance().getMessagePreViewDao().queryAllMessagePreView();
                observableEmitter.onNext(messagePreViewList);
            }
        }).subscribeOn(Schedulers.io())//被观察者在子线程
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<MessagePreView>>() {
            @Override
            public void accept(List<MessagePreView> messagePreViewList) throws Exception {
                if (mMessagePreViewList != null) {
                    mMessagePreViewList.clear();
                }

                mMessagePreViewList = messagePreViewList;

                if (mAdapter != null) {
                    mAdapter.reloadList(mMessagePreViewList);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter = new MessagePreViewAdapter(getContext(), mMessagePreViewList);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(ContextHelper.getContext()));
                    mRecyclerView.setAdapter(mAdapter);
                }
                if (mLoading != null) {
                    mLoading.setVisibility(View.GONE);
                }

                if (mMessagePreViewList.size() == 0) {
                    mNotMessageTip.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mNotMessageTip.setVisibility(View.GONE);
                }
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
    }

    private void initView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView_tabMessage);
        mLoading = mView.findViewById(R.id.loading_tab_message);
        mNotMessageTip = mView.findViewById(R.id.ll_not_message_tip);
        if (mAdapter == null) {
            mAdapter = new MessagePreViewAdapter(getContext(), mMessagePreViewList);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(ContextHelper.getContext()));
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onUpdate(int type, Object data) {
        if (type == NotifyReceiver.NOTIFY_TYPE_UPDATE_MESSAGE_PREVIEW) {//刷新消息页签
            loadMessagePreView();
        }
    }
}
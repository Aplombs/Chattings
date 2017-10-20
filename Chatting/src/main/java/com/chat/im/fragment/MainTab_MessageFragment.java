package com.chat.im.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chat.im.R;
import com.chat.im.adapter.MessagePreViewAdapter;
import com.chat.im.constant.Constants;
import com.chat.im.db.bean.MessagePreView;
import com.chat.im.helper.ContextHelper;
import com.chat.im.helper.DBHelper;
import com.chat.im.notify.NotifyHelper;
import com.chat.im.notify.NotifyMonitor;
import com.chat.im.notify.NotifyReceiver;
import com.chat.im.ui.SingleChatActivity;

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

public class MainTab_MessageFragment extends Fragment implements MessagePreViewAdapter.onItemClickListener, NotifyMonitor.NotifyMonitorListener {

    private RecyclerView mRecyclerView;
    private MessagePreViewAdapter mAdapter;
    private View mView, mNotMessageTip, mLoading;
    private List<MessagePreView> mMessagePreViewList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotifyMonitor.getInstance().registerNotifyMonitorListener(this);
    }

    private void loadMessagePreView() {
        Observable.create(new ObservableOnSubscribe<List<MessagePreView>>() {
            @Override
            public void subscribe(ObservableEmitter<List<MessagePreView>> observableEmitter) {
                List<MessagePreView> messagePreViewList = DBHelper.getInstance().getMessagePreViewDao().queryAllMessagePreView();
                observableEmitter.onNext(messagePreViewList);
            }
            //被观察者在子线程
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MessagePreView>>() {
                    @Override
                    public void accept(List<MessagePreView> messagePreViewList) throws Exception {
                        if (mMessagePreViewList != null) {
                            mMessagePreViewList.clear();
                        }

                        mMessagePreViewList = messagePreViewList;

                        if (mAdapter != null) {
                            mAdapter.reloadList(mMessagePreViewList);
                        } else {
                            mAdapter = new MessagePreViewAdapter(getContext(), mMessagePreViewList);
                        }

                        mRecyclerView.setLayoutManager(new LinearLayoutManager(ContextHelper.getContext()));
                        mRecyclerView.setAdapter(mAdapter);

                        if (mAdapter != null) {
                            mAdapter.setItemClick(MainTab_MessageFragment.this);
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
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //发生异常 展示暂无新消息
                        if (mLoading != null) {
                            mLoading.setVisibility(View.GONE);
                        }
                        mNotMessageTip.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_tab_message, null);
        initView();
        loadMessagePreView();
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
    }

    @Override
    public void onUpdate(int function, Object data) {
        //刷新消息页签item未读数 最后一条消息内容
        if (function == NotifyReceiver.NOTIFY_TYPE_UPDATE_MESSAGE_PREVIEW_ITEM_NOT_READ || function == NotifyReceiver.NOTIFY_TYPE_UPDATE_MESSAGE_PREVIEW_ITEM_CONTENT) {
            loadMessagePreView();
        }
    }

    //预览消息页签item点击监听 点击之后更新item未读消息数 底部tab未读数 打开单聊界面
    @Override
    public void onItemClick(int position) {
        //打开聊天界面
        Intent intent = new Intent(getContext(), SingleChatActivity.class);
        MessagePreView messagePreView = mMessagePreViewList.get(position);
        intent.putExtra(Constants.USER_ID, messagePreView.getMessagePreviewId());
        getContext().startActivity(intent);

        notifyMessageTabUpdate(position);
    }

    private void notifyMessageTabUpdate(final int position) {
        Observable.create(new ObservableOnSubscribe<Intent>() {
            @Override
            public void subscribe(ObservableEmitter<Intent> subscriber) throws Exception {
                //点击了当前item 将当前item未读数置为0
                mMessagePreViewList.get(position).setNotReadMessageNum("0");
                DBHelper.getInstance().getMessagePreViewDao().updateMessagePreView(mMessagePreViewList.get(position));

                int notReadNum = 0;
                for (int i = 0; i < mMessagePreViewList.size(); i++) {
                    MessagePreView messagePreView = mMessagePreViewList.get(i);
                    String notReadMessageNum = messagePreView.getNotReadMessageNum();
                    notReadNum += Integer.parseInt(TextUtils.isEmpty(notReadMessageNum) ? "0" : notReadMessageNum);
                }
                Intent intent = new Intent();
                intent.putExtra("notReadNum", notReadNum);

                subscriber.onNext(intent);
            }
        }).observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Intent>() {
                    @Override
                    public void accept(Intent intent) throws Exception {
                        //刷新消息页签底部未读数=每个item未读数相加
                        NotifyHelper.getInstance().notifyEvent(NotifyReceiver.NOTIFY_TYPE_UPDATE_MESSAGE_PREVIEW_TAB_NOT_READ, intent);
                        //刷新消息页签item未读数
                        NotifyHelper.getInstance().notifyEvent(NotifyReceiver.NOTIFY_TYPE_UPDATE_MESSAGE_PREVIEW_ITEM_NOT_READ, null);
                    }
                });
    }
}
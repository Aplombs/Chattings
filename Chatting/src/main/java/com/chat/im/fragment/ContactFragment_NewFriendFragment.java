package com.chat.im.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chat.im.R;
import com.chat.im.adapter.ContactNewFriendAdapter;
import com.chat.im.db.bean.WaitAddFriends;
import com.chat.im.helper.ContextHelper;
import com.chat.im.helper.DBHelper;
import com.chat.im.helper.OKHttpClientHelper;
import com.chat.im.helper.UIHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 联系人页签--新朋友
 */

public class ContactFragment_NewFriendFragment extends Fragment implements OKHttpClientHelper.ResponseListener, ContactNewFriendAdapter.DegreeItemClickListener {

    private View mView, mLoading;
    private Dialog loadingDialog;
    private RecyclerView mRecyclerView;
    private ContactNewFriendAdapter mAdapter;
    private List<WaitAddFriends> mWaitAddFriendList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载待添加的好友
        Observable.create(new ObservableOnSubscribe<List<WaitAddFriends>>() {
            @Override
            public void subscribe(ObservableEmitter<List<WaitAddFriends>> observableEmitter) {
                mWaitAddFriendList.clear();
                mWaitAddFriendList = DBHelper.getInstance().getDaoSession().
                        getWaitAddFriendsDao().queryBuilder().list();
                observableEmitter.onNext(mWaitAddFriendList);
            }
        }).subscribeOn(Schedulers.io())//被观察者在子线程
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<WaitAddFriends>>() {
            @Override
            public void accept(List<WaitAddFriends> waitAddFriendsList) throws Exception {
                if (mLoading == null) {
                    return;
                }
                initAdapter(true);
                mRecyclerView.setVisibility(View.VISIBLE);
                mLoading.setVisibility(View.GONE);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView(inflater);
        return mView;
    }

    @Override
    public void onItemDegreeClick(int position) {

    }

    @Override
    public void onResponse(int requestCode, Object response) {
        initAdapter(false);
        switch (requestCode) {
        }
    }

    @Override
    public void onFailure(int requestCode, int statusCode) {

    }

    private void initView(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_contact_new_friend, null);
        mLoading = mView.findViewById(R.id.loading_contact_new_friend);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView_contact_new_friend);

        if (loadingDialog == null) {
            loadingDialog = UIHelper.getInstance().createLoadingDialog(getActivity());
        }
    }

    private void initAdapter(boolean isCreate) {
        if (isCreate) {
            mAdapter = new ContactNewFriendAdapter(mWaitAddFriendList);
            mAdapter.setOnDegreeClick(this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(ContextHelper.getContext()));
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.reloadList(mWaitAddFriendList);
        }
    }

    private void showLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.show();
        }
    }

    private void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
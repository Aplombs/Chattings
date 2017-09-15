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
import com.chat.im.constant.Constants;
import com.chat.im.db.bean.ContactInfo;
import com.chat.im.db.bean.WaitAddFriends;
import com.chat.im.helper.ContextHelper;
import com.chat.im.helper.DBHelper;
import com.chat.im.helper.LogHelper;
import com.chat.im.helper.OKHttpClientHelper;
import com.chat.im.helper.UIHelper;
import com.chat.im.ui.NewFriendOrGroupChatOrPublicChatActivity;

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

public class ContactFragment_NewFriendFragment extends Fragment implements OKHttpClientHelper.ResponseListener, ContactNewFriendAdapter.AgreeItemClickListener, NewFriendOrGroupChatOrPublicChatActivity.OnBackListener {

    private final String TAG = "[ContactFragment_NewFriendFragment] ";
    private View mView, mLoading;
    private Dialog loadingDialog;
    private RecyclerView mRecyclerView;
    private boolean isClickAgree = false;//是否点击过接受添加联系人,判断是否刷新联系人界面
    private WaitAddFriends waitAddFriends;
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
    public void onResume() {
        super.onResume();
        OKHttpClientHelper.getInstance().setResponseListener(this);
        ((NewFriendOrGroupChatOrPublicChatActivity) getActivity()).setOnBackListener(this);
    }

    @Override
    public void onItemAgreeClick(int position) {
        if (!UIHelper.getInstance().checkNetwork()) return;
        showLoadingDialog();
        waitAddFriends = mWaitAddFriendList.get(position);
        OKHttpClientHelper.getInstance().agreeAddFriend(waitAddFriends.getUserId());
    }

    @Override
    public void onResponse(int requestCode, Object response) {
        switch (requestCode) {
            case Constants.OK_AGREE_FRIEND_REQUEST:
                isClickAgree = true;
                //将此待添加联系人从待添加数据库删除并且添加到联系人数据库
                deleteWaitFriendAndInsertDao();
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, int statusCode) {
        switch (requestCode) {
            case Constants.FAILURE_AGREE_FRIEND_REQUEST:
                hideLoadingDialog();
                UIHelper.getInstance().toast("添加失败,稍后重试");
                break;
        }
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
            mAdapter.setOnAgreeClick(this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(ContextHelper.getContext()));
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.reloadList(mWaitAddFriendList);
        }
    }

    private void deleteWaitFriendAndInsertDao() {
        //从数据库将待添加好友删除并将好友信息添加到联系人数据库
        Observable.create(new ObservableOnSubscribe<List<WaitAddFriends>>() {
            @Override
            public void subscribe(ObservableEmitter<List<WaitAddFriends>> observableEmitter) {
                waitAddFriends.setIsAdded(true);
                DBHelper.getInstance().getDaoSession().getWaitAddFriendsDao().insertOrReplace(waitAddFriends);

                ContactInfo contactInfo = new ContactInfo(waitAddFriends.getUserId(), waitAddFriends.getRegion(), waitAddFriends.getPhone(), waitAddFriends.getHeadUri(), waitAddFriends.getNickName(), waitAddFriends.getRemarkName(), waitAddFriends.getShowName(), waitAddFriends.getShowNameLetter());
                DBHelper.getInstance().getDaoSession().getContactInfoDao().insertOrReplaceInTx(contactInfo);

                mWaitAddFriendList.clear();
                mWaitAddFriendList = DBHelper.getInstance().getDaoSession().
                        getWaitAddFriendsDao().queryBuilder().list();
                observableEmitter.onNext(mWaitAddFriendList);
            }
        }).subscribeOn(Schedulers.io())//被观察者在子线程
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<WaitAddFriends>>() {
            @Override
            public void accept(List<WaitAddFriends> waitAddFriendsList) throws Exception {
                hideLoadingDialog();
                UIHelper.getInstance().toast("已添加到联系人");

                initAdapter(false);
            }
        });
    }

    //返回的时候setResult,联系人界面刷新
    @Override
    public void onBack() {
        LogHelper.e(TAG + "是否刷新联系人界面--->>> " + isClickAgree);
        if (isClickAgree) {
            getActivity().setResult(MainTab_ContactFragment.START_RESULT_CODE);
        }
        getActivity().finish();
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
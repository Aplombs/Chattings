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
import com.chat.im.adapter.ContactAdapter;
import com.chat.im.db.bean.ContactInfo;
import com.chat.im.helper.ContextHelper;
import com.chat.im.helper.DBHelper;
import com.chat.im.helper.OKHttpClientHelper;
import com.chat.im.helper.UtilsHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 联系人页签
 */

public class ContactFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private View mLoading, mData;
    private ContactAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<ContactInfo> mContactInfoList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.create(new ObservableOnSubscribe<List<ContactInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ContactInfo>> observableEmitter) throws Exception {
                //获取全部好友信息
                if (UtilsHelper.getInstance().isNetworkConnected()) {
                    OKHttpClientHelper.getInstance().getAllContact();
                }
                List<ContactInfo> mContactInfoList = DBHelper.getInstance().getDaoSession().
                        getContactInfoDao().queryBuilder().list();
                Collections.sort(mContactInfoList, new Comparator<ContactInfo>() {
                    @Override
                    public int compare(ContactInfo contactInfo1, ContactInfo contactInfo2) {
                        //按照昵称的首字母排序
                        return contactInfo1.getNickNameSpelling().compareTo(contactInfo2.getNickNameSpelling());
                    }
                });
                observableEmitter.onNext(mContactInfoList);
            }
        }).subscribeOn(Schedulers.io())//被观察者在子线程
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<ContactInfo>>() {
            @Override
            public void accept(List<ContactInfo> contactInfoList) throws Exception {
                if (mLoading == null || mData == null) {
                    return;
                }
                mData.setVisibility(View.VISIBLE);
                mLoading.setVisibility(View.GONE);
                mAdapter = new ContactAdapter(contactInfoList);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(ContextHelper.getContext()));
                mRecyclerView.setAdapter(mAdapter);
            }
        });
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
        mLoading = mView.findViewById(R.id.loading_Contact);
        mData = mView.findViewById(R.id.data);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView_tabContact);
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

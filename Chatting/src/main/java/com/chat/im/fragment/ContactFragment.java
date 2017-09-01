package com.chat.im.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.adapter.ContactAdapter;
import com.chat.im.db.bean.ContactInfo;
import com.chat.im.db.dao.ContactInfoDao;
import com.chat.im.helper.DBHelper;
import com.chat.im.helper.LogHelper;
import com.chat.im.helper.OKHttpClientHelper;
import com.chat.im.helper.UtilsHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private View mView, mLoading;
    private TextView mContactNum;
    private ExpandableListView mExpandableListView;

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
                //initTestData();
                List<ContactInfo> mContactInfoList = DBHelper.getInstance().getDaoSession().
                        getContactInfoDao().queryBuilder().list();
                observableEmitter.onNext(mContactInfoList);
            }
        }).subscribeOn(Schedulers.io())//被观察者在子线程
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<ContactInfo>>() {
            @Override
            public void accept(List<ContactInfo> infoList) throws Exception {
                if (mLoading == null) {
                    return;
                }

                Map<String, List<ContactInfo>> map = new HashMap<>();

                //字母集合
                List<String> letterList = new ArrayList<>();
                for (ContactInfo contactInfo : infoList) {
                    String nameLetter = contactInfo.getShowNameLetter();
                    if (!letterList.contains(nameLetter)) {
                        letterList.add(nameLetter);
                    }
                }

                //集合排序
                Collections.sort(letterList);
                LogHelper.e("list sort " + letterList);
                if (letterList.contains("#")) {
                    //将集合进行旋转(负数正向移动 正数反向移动)
                    Collections.rotate(letterList, -1);
                    LogHelper.e("list rotate " + letterList);
                }

                for (int i = 0; i < letterList.size(); i++) {
                    //每个字母所对应的联系人信息集合
                    List<ContactInfo> contactList = new ArrayList<>();
                    String letter = letterList.get(i);
                    //遍历联系人集合匹配哪个联系人的首字母和letter一样
                    for (int j = 0; j < infoList.size(); j++) {
                        String firstNameLetter = infoList.get(j).getShowNameLetter();
                        if (letter.equals(firstNameLetter)) {
                            contactList.add(infoList.get(j));
                        }
                    }
                    map.put(letter, contactList);
                }

                mLoading.setVisibility(View.GONE);
                mContactNum.setText(String.valueOf(infoList.size() + "位联系人"));
                mExpandableListView.setVisibility(View.VISIBLE);
                ContactAdapter adapter2 = new ContactAdapter(letterList, map);
                mExpandableListView.setAdapter(adapter2);
                //默认都打开
                for (int i = 0; i < adapter2.getGroupCount(); i++) {
                    mExpandableListView.expandGroup(i);
                }
                //字母不可点击
                mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        return true;
                    }
                });
            }
        });
    }

    //添加测试数据
    private void initTestData() {
        ContactInfoDao contactInfoDao = DBHelper.getInstance().getDaoSession().getContactInfoDao();
        String[] list = getResources().getStringArray(R.array.provinces);
        for (int i = 0; i < list.length; i++) {
            String name = list[i];
            String showNameLetter = UtilsHelper.getInstance().getFirstLetter(name);
            ContactInfo contactInfo = new ContactInfo(String.valueOf(i), "", "", "", name, "", name, showNameLetter);
            contactInfoDao.insertOrReplaceInTx(contactInfo);
        }
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
    }

    private void initView(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_contact, null);

        mLoading = mView.findViewById(R.id.loading_Contact);
        mExpandableListView = (ExpandableListView) mView.findViewById(R.id.expandableListView_tabContact);

        View headerView = inflater.inflate(R.layout.listview_header_view_contact, null);
        View footerView = inflater.inflate(R.layout.listview_footer_view_contact, null);

        headerView.findViewById(R.id.newFriend_Contact).setOnClickListener(ContactFragment.this);
        headerView.findViewById(R.id.groupChat_Contact).setOnClickListener(ContactFragment.this);
        headerView.findViewById(R.id.publicChat_Contact).setOnClickListener(ContactFragment.this);

        mContactNum = (TextView) footerView.findViewById(R.id.contactNum_Contact);

        mExpandableListView.addHeaderView(headerView);
        mExpandableListView.addFooterView(footerView);
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

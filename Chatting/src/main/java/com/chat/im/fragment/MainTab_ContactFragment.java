package com.chat.im.fragment;

import android.content.Intent;
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
import com.chat.im.constant.Constants;
import com.chat.im.db.bean.ContactInfo;
import com.chat.im.db.bean.WaitAddFriends;
import com.chat.im.db.dao.ContactInfoDao;
import com.chat.im.helper.DBHelper;
import com.chat.im.helper.LogHelper;
import com.chat.im.helper.OKHttpClientHelper;
import com.chat.im.helper.SpHelper;
import com.chat.im.helper.UtilsHelper;
import com.chat.im.ui.UserInfoDetailActivity;

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

public class MainTab_ContactFragment extends Fragment implements View.OnClickListener, ContactAdapter.ContactItemClickListener {

    public static final int START_USER_DETAIL_RESULT_CODE = 999;
    private final int START_USER_DETAIL_REQUEST_CODE = 99;
    private View mView, mLoading;
    private TextView mContactNum, mNewFriendNum;
    private ExpandableListView mExpandableListView;
    private ContactAdapter mContactAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.create(new ObservableOnSubscribe<Map>() {
            @Override
            public void subscribe(ObservableEmitter<Map> observableEmitter) {
                if (UtilsHelper.getInstance().isNetworkConnected()) {
                    try {
                        //获取全部好友信息--获取成功之后会把之前所有联系人数据delete
                        OKHttpClientHelper.getInstance().getAllContact();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //添加自己为好友
                addMySelf();

                Map<String, List> map = new HashMap<>();
                List<ContactInfo> mContactInfoList = DBHelper.getInstance().getDaoSession().
                        getContactInfoDao().queryBuilder().list();
                List<WaitAddFriends> waitAddFriendsList = DBHelper.getInstance().getDaoSession().
                        getWaitAddFriendsDao().queryBuilder().list();
                map.put("contact", mContactInfoList);
                map.put("newContact", waitAddFriendsList);
                observableEmitter.onNext(map);
            }
        }).subscribeOn(Schedulers.io())//被观察者在子线程
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Map>() {
            @Override
            public void accept(Map map) throws Exception {
                if (mLoading == null) {
                    return;
                }
                List<ContactInfo> mContactInfoList = (List<ContactInfo>) map.get("contact");
                List<WaitAddFriends> waitAddFriendsList = (List<WaitAddFriends>) map.get("newContact");

                mNewFriendNum.setVisibility(View.VISIBLE);
                mNewFriendNum.setText(String.valueOf(waitAddFriendsList.size()));

                initContactAdapter(mContactInfoList, true);

                mLoading.setVisibility(View.GONE);
                mExpandableListView.setVisibility(View.VISIBLE);
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
    }

    @Override
    public void onItemClick(String contactId) {
        goToUserInfoDetail(contactId);
    }

    private void initView(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_tab_contact, null);

        mLoading = mView.findViewById(R.id.loading_Contact);
        mExpandableListView = (ExpandableListView) mView.findViewById(R.id.expandableListView_tabContact);

        View headerView = inflater.inflate(R.layout.listview_header_view_contact, null);
        View footerView = inflater.inflate(R.layout.listview_footer_view_contact, null);

        headerView.findViewById(R.id.newFriend_Contact).setOnClickListener(MainTab_ContactFragment.this);
        headerView.findViewById(R.id.groupChat_Contact).setOnClickListener(MainTab_ContactFragment.this);
        headerView.findViewById(R.id.publicChat_Contact).setOnClickListener(MainTab_ContactFragment.this);

        mNewFriendNum = (TextView) headerView.findViewById(R.id.not_read_tab_contact);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (START_USER_DETAIL_REQUEST_CODE == requestCode && START_USER_DETAIL_RESULT_CODE == resultCode) {
            //刷新好友列表
            List<ContactInfo> mContactInfoList = DBHelper.getInstance().getDaoSession().
                    getContactInfoDao().queryBuilder().list();
            initContactAdapter(mContactInfoList, false);

        }
    }

    //初始化联系人Adapter
    private void initContactAdapter(List<ContactInfo> infoList, boolean isCreate) {
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

        mContactNum.setText(String.valueOf(infoList.size() + "位联系人"));

        if (isCreate) {
            mContactAdapter = new ContactAdapter(getActivity(), letterList, map);
            mExpandableListView.setAdapter(mContactAdapter);
        } else {
            mContactAdapter.reLoadData(letterList, map);
        }

        mContactAdapter.setOnItemClick(MainTab_ContactFragment.this);

        //默认都打开
        for (int i = 0; i < mContactAdapter.getGroupCount(); i++) {
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

    //打开好友详细资料界面
    private void goToUserInfoDetail(String id) {
        Intent intent = new Intent(getContext(), UserInfoDetailActivity.class);
        intent.putExtra(Constants.USER_ID, id);
        startActivityForResult(intent, START_USER_DETAIL_REQUEST_CODE);
    }

    //将自己加入联系人列表
    private void addMySelf() {
        String userId = SpHelper.getInstance().get(Constants.SP_LOGIN_USERID, "");
        String region = SpHelper.getInstance().get(Constants.SP_LOGIN_PHONE_REGION, "");
        String phone = SpHelper.getInstance().get(Constants.SP_LOGIN_PHONE, "");
        String userHeadUri = SpHelper.getInstance().get(Constants.SP_LOGIN_HEAD_URI, "");
        String nickname = SpHelper.getInstance().get(Constants.SP_LOGIN_NICKNAME, "");
        String showNameLetter = UtilsHelper.getInstance().getFirstLetter(nickname);
        ContactInfo contactInfo = new ContactInfo(userId, region, phone, userHeadUri, nickname, "", nickname, showNameLetter);

        ContactInfoDao contactInfoDao = DBHelper.getInstance().getDaoSession().getContactInfoDao();
        contactInfoDao.insertOrReplaceInTx(contactInfo);
    }
}

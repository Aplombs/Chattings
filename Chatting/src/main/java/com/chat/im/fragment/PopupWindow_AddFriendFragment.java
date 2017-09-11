package com.chat.im.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.constant.Constants;
import com.chat.im.helper.OKHttpClientHelper;
import com.chat.im.helper.SpHelper;
import com.chat.im.helper.UIHelper;
import com.chat.im.helper.UtilsHelper;
import com.chat.im.jsonbean.GetUserInfoByPhoneResponse;
import com.chat.im.ui.UserInfoDetailActivity;

/**
 * popupWindow-添加好友
 */

public class PopupWindow_AddFriendFragment extends Fragment implements View.OnClickListener, OKHttpClientHelper.ResponseListener {

    private View mView;
    private EditText et_phone;
    private ImageView mUserHead;
    private View mll_SearchFriendInfo;
    private TextView mMyPhone, mUserName;
    private Dialog loadingDialog;
    private String mSearchUserId, mSearchNickname, mSearchHeadUri;
    private String mSearchPhone;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OKHttpClientHelper.getInstance().setResponseListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView(inflater);
        return mView;
    }

    private void initView(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_popup_add_friend, null);
        mMyPhone = (TextView) mView.findViewById(R.id.my_phone_search_friend);
        et_phone = (EditText) mView.findViewById(R.id.phone_search_friend);
        mll_SearchFriendInfo = mView.findViewById(R.id.ll_search_friend_info);
        mUserName = (TextView) mView.findViewById(R.id.userNickName_search_friend);
        mUserHead = (ImageView) mView.findViewById(R.id.userHead_search_friend);

        mView.findViewById(R.id.my_qrcode_search_friend).setOnClickListener(this);
        mView.findViewById(R.id.search_search_friend).setOnClickListener(this);
        mView.findViewById(R.id.ll_search_friend_info).setOnClickListener(this);

        String phone = SpHelper.getInstance().get(Constants.SP_LOGIN_PHONE, "");
        phone = UtilsHelper.getInstance().formatPhone(phone);
        mMyPhone.setText(String.format("我的手机号:%s", phone));

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = s.toString().trim();
                int length = phone.length();
                if (length == 11) {
                    searchFriend(); //输入内容11位的时候调用
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchFriend();//键盘搜索按钮
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_search_friend://搜索好友
                searchFriend();//搜索按钮
                break;
            case R.id.my_qrcode_search_friend://我的二维码
                UIHelper.getInstance().toast("我的二维码");
                break;
            case R.id.ll_search_friend_info://搜索到的好友,点击发送添加好友
                GoToUserInfoDetail();
                break;
        }
    }

    //打开好友详细资料界面
    private void GoToUserInfoDetail() {
        Intent intent = new Intent(getActivity(), UserInfoDetailActivity.class);
        intent.putExtra(Constants.USER_PHONE, mSearchPhone);
        intent.putExtra(Constants.USER_ID, mSearchUserId);
        intent.putExtra(Constants.USER_NICK_NAME, mSearchNickname);
        intent.putExtra(Constants.USER_HEAD_URI, mSearchHeadUri);
        startActivity(intent);
    }

    @Override
    public void onResponse(int requestCode, Object response) {
        switch (requestCode) {
            case Constants.OK_SEARCH_FRIEND:
                loadingDialog.dismiss();
                GetUserInfoByPhoneResponse phoneResponse = (GetUserInfoByPhoneResponse) response;
                mll_SearchFriendInfo.setVisibility(View.VISIBLE);
                mSearchUserId = phoneResponse.getResult().getId();
                mSearchNickname = phoneResponse.getResult().getNickname();
                mSearchHeadUri = phoneResponse.getResult().getPortraitUri();
                mUserName.setText(mSearchNickname);
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, int statusCode) {
        switch (requestCode) {
            case Constants.FAILURE_SEARCH_FRIEND:
                loadingDialog.dismiss();
                mll_SearchFriendInfo.setVisibility(View.GONE);
                if (statusCode == Constants.FAILURE_TYPE_SEARCH_FRIEND_NOT_EXIST) {//好友不存在
                    UIHelper.getInstance().toast("该用户不存在");
                } else {
                    UIHelper.getInstance().toast("搜索失败,稍后重试");
                }
                break;
        }
    }

    private void searchFriend() {
        //检查网络连接
        if (!UIHelper.getInstance().checkNetwork()) {
            return;
        }
        mSearchPhone = et_phone.getText().toString();
        int length = mSearchPhone.length();
        //此处再次判断手机号是否11位是为了用户点击后面的搜索按钮
        if (length != 11 || !UtilsHelper.getInstance().isMobile(mSearchPhone)) {
            UIHelper.getInstance().toast("请输入正确的手机号");
            return;
        }
        UIHelper.getInstance().hideSoftInput(et_phone);
        loadingDialog = UIHelper.getInstance().createLoadingDialog(getActivity());
        loadingDialog.show();
        //手机号正确自动搜索好友
        OKHttpClientHelper.getInstance().searchFriendByPhone(Constants.REGION, mSearchPhone);
    }
}

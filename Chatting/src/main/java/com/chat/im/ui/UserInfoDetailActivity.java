package com.chat.im.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.application.IMApp;
import com.chat.im.constant.Constants;
import com.chat.im.db.bean.ContactInfo;
import com.chat.im.db.dao.ContactInfoDao;
import com.chat.im.helper.DBHelper;
import com.chat.im.helper.OKHttpClientHelper;
import com.chat.im.helper.SpHelper;
import com.chat.im.helper.UIHelper;
import com.chat.im.helper.UtilsHelper;

/**
 * 用户详细资料界面
 */

public class UserInfoDetailActivity extends BaseActivity implements View.OnClickListener, OKHttpClientHelper.ResponseListener {

    private String userID;
    private Intent mIntent;
    private View sendMessage;
    private ImageView mUserHead;
    private View requestAddFriend;
    private Handler handler = new Handler();
    private Dialog loadingDialog, friendSettingDialog;
    private TextView mUserRemarkName, mUserPhone, mUserNickName;

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_user_info_detail;
    }

    @Override
    protected void init() {
        mTitleName.setText("详细资料");
        mBt_Add.setVisibility(View.GONE);
        mReturnView.setVisibility(View.VISIBLE);

        OKHttpClientHelper.getInstance().setResponseListener(this);

        mIntent = getIntent();
        userID = mIntent.getStringExtra(Constants.USER_ID);

        mUserHead = (ImageView) findViewById(R.id.userHead_user_detail);
        mUserRemarkName = (TextView) findViewById(R.id.userRemarkName_user_detail);
        mUserNickName = (TextView) findViewById(R.id.userNickName_user_detail);
        mUserPhone = (TextView) findViewById(R.id.userPhone_user_detail);
        sendMessage = findViewById(R.id.send_message_user_detail);
        requestAddFriend = findViewById(R.id.request_add_friend_user_detail);

        ContactInfo contactInfo = DBHelper.getInstance().getDaoSession().getContactInfoDao().queryBuilder().where(ContactInfoDao.Properties.UserId.eq(userID)).unique();

        if (contactInfo != null) {
            isMyContact(contactInfo);
        } else {
            isNotContact();
        }

        if (loadingDialog == null) {
            loadingDialog = UIHelper.getInstance().createLoadingDialog(this);
        }
        if (friendSettingDialog == null) {
            friendSettingDialog = UIHelper.getInstance().createFriendSettingDialog(this);
        }
    }

    private void isMyContact(ContactInfo contactInfo) {
        String remarkName = contactInfo.getRemarkName();
        String nickName = contactInfo.getNickName();
        String phone = contactInfo.getPhone();

        //优先展示备注 没有备注展示昵称;有备注的话备注名在第一行展示,昵称在第二行
        if (!TextUtils.isEmpty(remarkName)) {
            mUserRemarkName.setText(remarkName);
            mUserNickName.setVisibility(View.VISIBLE);
            mUserNickName.setText(String.format("昵称:%s", nickName));
        } else {//没有备注的话隐藏展示昵称的控件
            mUserNickName.setVisibility(View.GONE);
            mUserRemarkName.setText(nickName);
        }

        mUserPhone.setText(UtilsHelper.getInstance().formatPhone(phone));

        sendMessage.setVisibility(View.VISIBLE);
        sendMessage.setOnClickListener(this);
        requestAddFriend.setVisibility(View.GONE);

        //自己不展示更多
        if (IMApp.getInstance().mLoginID.equals(userID)) {
            mBt_More.setVisibility(View.GONE);
        } else {
            mBt_More.setVisibility(View.VISIBLE);
            mBt_More.setOnClickListener(this);
        }

        if (friendSettingDialog != null) {
            friendSettingDialog.findViewById(R.id.setRemarkName_friendSetting).setOnClickListener(this);
            friendSettingDialog.findViewById(R.id.recommendedContact_friendSetting).setOnClickListener(this);
            friendSettingDialog.findViewById(R.id.deleteContact_friendSetting).setOnClickListener(this);
            friendSettingDialog.findViewById(R.id.addBlack_friendSetting).setOnClickListener(this);
            friendSettingDialog.findViewById(R.id.more_friendSetting).setOnClickListener(this);
        }
    }

    private void isNotContact() {
        String phone = mIntent.getStringExtra(Constants.USER_PHONE);
        String nickName = mIntent.getStringExtra(Constants.USER_NICK_NAME);
        String userHeadUri = mIntent.getStringExtra(Constants.USER_HEAD_URI);

        mUserNickName.setVisibility(View.GONE);
        mUserRemarkName.setText(nickName);

        mUserPhone.setText(UtilsHelper.getInstance().formatPhone(phone));

        mBt_More.setVisibility(View.GONE);

        sendMessage.setVisibility(View.GONE);
        requestAddFriend.setOnClickListener(this);
        requestAddFriend.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_more://右上角更多按钮
                friendSetting();
                break;
            case R.id.setRemarkName_friendSetting://右上角更多按钮--设置备注
                break;
            case R.id.recommendedContact_friendSetting://右上角更多按钮--推荐联系人
                break;
            case R.id.deleteContact_friendSetting://右上角更多按钮--删除联系人
                break;
            case R.id.addBlack_friendSetting://右上角更多按钮--加入黑名单
                break;
            case R.id.more_friendSetting://右上角更多按钮--更多
                break;
            case R.id.send_message_user_detail:
                openChatActivity();
                break;
            case R.id.request_add_friend_user_detail:
                sendAddFriendRequest();
                break;
        }
    }

    //好友设置
    private void friendSetting() {
        if (friendSettingDialog != null) {
            friendSettingDialog.show();
        }
    }

    //打开聊天界面
    private void openChatActivity() {

    }

    //发送添加好友请求
    private void sendAddFriendRequest() {
        String nickName = SpHelper.getInstance().get(Constants.SP_LOGIN_NICKNAME, "");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加到联系人");
        View view = View.inflate(this, R.layout.dialog_add_friend, null);
        final EditText editText = (EditText) view.findViewById(R.id.addFriend_request_message);
        editText.setText(String.format("我是%s", nickName));
        builder.setView(view);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadingDialog.show();
                String requestMessage = editText.getText().toString();
                OKHttpClientHelper.getInstance().sendAddFriendRequest(userID, requestMessage);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        //editText必须获取焦点 editText可见 alertDialog界面绘制完成才能弹出软键盘;虽然有焦点可见但是未必alertDialog界面全部绘制完成
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                UIHelper.getInstance().showSoftInput(editText);
                String requestMessage = editText.getText().toString();
                editText.setSelection(2, requestMessage.trim().length());
            }
        }, 200);
    }

    @Override
    public void onResponse(int requestCode, Object response) {
        switch (requestCode) {
            case Constants.OK_ADD_FRIEND_REQUEST:
                loadingDialog.dismiss();
                UIHelper.getInstance().toast("已发送添加");
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, int statusCode) {
        switch (requestCode) {
            case Constants.FAILURE_ADD_FRIEND_REQUEST:
                loadingDialog.dismiss();
                UIHelper.getInstance().toast("添加到联系人失败,稍后重试");
                break;
        }
    }
}
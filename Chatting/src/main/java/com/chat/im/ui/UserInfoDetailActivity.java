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
import com.chat.im.fragment.MainTab_ContactFragment;
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
    private ContactInfo mContactInfo;

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

        mContactInfo = DBHelper.getInstance().getContactDao().queryContact(userID);

        if (loadingDialog == null) {
            loadingDialog = UIHelper.getInstance().createLoadingDialog(this);
        }
        if (friendSettingDialog == null) {
            friendSettingDialog = UIHelper.getInstance().createFriendSettingDialog(this);
        }

        if (mContactInfo != null) {
            isMyContact();
        } else {
            isNotContact();
        }
    }

    private void isMyContact() {
        String remarkName = mContactInfo.getRemarkName();
        String nickName = mContactInfo.getNickName();
        String phone = mContactInfo.getPhone();

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
                deleteFriend();
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

    @Override
    public void onResponse(int requestCode, Object response) {
        switch (requestCode) {
            case Constants.OK_ADD_FRIEND_REQUEST://添加好友
                hideLoadingDialog();
                UIHelper.getInstance().toast("已发送添加");
                break;
            case Constants.OK_DELETE_FRIEND_REQUEST://删除好友
                deleteFriendFromDao();
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, int statusCode) {
        switch (requestCode) {
            case Constants.FAILURE_ADD_FRIEND_REQUEST://添加好友
                hideLoadingDialog();
                UIHelper.getInstance().toast("添加到联系人失败,稍后重试");
                break;
            case Constants.FAILURE_DELETE_FRIEND_REQUEST://删除好友
                hideLoadingDialog();
                UIHelper.getInstance().toast("删除失败,稍后重试");
                break;
        }
    }

    //打开聊天界面
    private void openChatActivity() {
        Intent intent = new Intent(this, SingleChatActivity.class);
        intent.putExtra(Constants.USER_ID, userID);
        startActivity(intent);
        this.finish();
    }

    //删除好友
    private void deleteFriend() {
        hideFriendSettingDialog();
        if (!UIHelper.getInstance().checkNetwork()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除联系人");
        builder.setMessage("将联系人" + mContactInfo.getShowName() + "删除,将同时删除与该联系人的聊天记录");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showLoadingDialog();
                OKHttpClientHelper.getInstance().deleteFriend(userID);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.red));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.text_gray));
    }

    //从数据库将好友删除
    private void deleteFriendFromDao() {
        if (DBHelper.getInstance().getContactDao().deleteContact(userID)) {
            UIHelper.getInstance().toast("已删除");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setResult(MainTab_ContactFragment.START_RESULT_CODE);
                    UserInfoDetailActivity.this.finish();
                }
            }, 500);
        } else {
            UIHelper.getInstance().toast("删除失败,请稍后重试");
        }
        hideLoadingDialog();
    }

    //好友设置
    private void friendSetting() {
        showFriendSettingDialog();
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
                showLoadingDialog();
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

    private void showFriendSettingDialog() {
        if (friendSettingDialog != null) {
            friendSettingDialog.show();
        }
    }

    private void hideFriendSettingDialog() {
        if (friendSettingDialog != null) {
            friendSettingDialog.dismiss();
        }
    }
}
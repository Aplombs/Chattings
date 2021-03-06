package com.chat.im.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.constant.Constants;
import com.chat.im.fragment.MainTab_ContactFragment;
import com.chat.im.fragment.MainTab_MeFragment;
import com.chat.im.fragment.MainTab_MessageFragment;
import com.chat.im.notify.NotifyMonitor;
import com.chat.im.notify.NotifyReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 */

public class MainActivity extends BaseActivity implements View.OnClickListener, NotifyMonitor.NotifyMonitorListener {

    protected PopupWindow mPopupWindow;
    private ViewPager mViewPager;
    private ImageView mImage_tabMessage, mImage_tabContact, mImage_tabMe;
    private View mLoading, mTip_me, mTabMessage, mTabContact, mTabMe, mPopupView, mPopupWindowLocationView;
    private TextView mText_tabMessage, mText_tabContact, mText_tabMe, mNotRead_tabMessage;

    @Override
    protected void init() {

        NotifyMonitor.getInstance().registerNotifyMonitorListener(this);

        mBt_Add.setVisibility(View.VISIBLE);
        mReturnView.setVisibility(View.GONE);

        mLoading = findViewById(R.id.title_bar_loading);

        mPopupWindowLocationView = findViewById(R.id.popup_add_location);
        mPopupView = LayoutInflater.from(this).inflate(R.layout.popup_window_layout_add, null);

        mTabMessage = findViewById(R.id.tab_message_main);
        mTabContact = findViewById(R.id.tab_contact_main);
        mTabMe = findViewById(R.id.tab_me_main);

        mImage_tabMessage = (ImageView) findViewById(R.id.image_tab_message);
        mImage_tabContact = (ImageView) findViewById(R.id.image_tab_contact);
        mImage_tabMe = (ImageView) findViewById(R.id.image_tab_me);
        mText_tabMessage = (TextView) findViewById(R.id.text_tab_message);
        mText_tabContact = (TextView) findViewById(R.id.text_tab_contact);
        mText_tabMe = (TextView) findViewById(R.id.text_tab_me);
        mNotRead_tabMessage = (TextView) findViewById(R.id.not_read_tab_message);
        mTip_me = findViewById(R.id.tip_tab_me);

        mViewPager = (ViewPager) findViewById(R.id.viewpager_main);

        mPopupView.findViewById(R.id.popup_group_chat).setOnClickListener(this);
        mPopupView.findViewById(R.id.popup_add_friend).setOnClickListener(this);
        mPopupView.findViewById(R.id.popup_scanner).setOnClickListener(this);
        mPopupView.findViewById(R.id.popup_collect_payment).setOnClickListener(this);

        mBt_Add.setOnClickListener(this);
        mTabMessage.setOnClickListener(this);
        mTabContact.setOnClickListener(this);
        mTabMe.setOnClickListener(this);

        initViewPager();

        mViewPager.setCurrentItem(0);
        selectTab(0);
    }

    private void initViewPager() {
        MainTab_MessageFragment messageFragment = new MainTab_MessageFragment();
        MainTab_ContactFragment contactFragment = new MainTab_ContactFragment();
        MainTab_MeFragment meFragment = new MainTab_MeFragment();

        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(messageFragment);
        fragmentList.add(contactFragment);
        fragmentList.add(meFragment);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_add:
                showPopupWindow();
                break;
            case R.id.tab_message_main:
                selectTab(0);
                break;
            case R.id.tab_contact_main:
                selectTab(1);
                break;
            case R.id.tab_me_main:
                selectTab(2);
                break;
            //发起群聊
            case R.id.popup_group_chat:
                goToPopupWindow(Constants.TAG_INITIATE_GROUP_CHAT_POPUP_WINDOW);
                break;
            //添加好友
            case R.id.popup_add_friend:
                goToPopupWindow(Constants.TAG_ADD_FRIEND_POPUP_WINDOW);
                break;
            //扫一扫
            case R.id.popup_scanner:
                goToPopupWindow(Constants.TAG_SCANNER_POPUP_WINDOW);
                break;
            //收付款
            case R.id.popup_collect_payment:
                goToPopupWindow(Constants.TAG_COLLECT_PAYMENT_POPUP_WINDOW);
                break;
            default:
                break;
        }
    }

    //点击popup条目,打开对应界面
    private void goToPopupWindow(String tag) {
        mPopupWindow.dismiss();
        Intent intent = new Intent(this, PopupWindowActivity.class);
        intent.putExtra("tag", tag);
        startActivity(intent);
    }

    private void selectTab(int position) {
        switch (position) {
            case 0:
                //根据收取消息状态改变title
                mTitleName.setText("消息");
                mBt_Add.setVisibility(View.VISIBLE);
                mLoading.setVisibility(View.GONE);

                mImage_tabMessage.setImageResource(R.drawable.tab_message_1);
                mImage_tabContact.setImageResource(R.drawable.tab_contact_0);
                mImage_tabMe.setImageResource(R.drawable.tab_me_0);

                mText_tabMessage.setTextColor(getResources().getColor(R.color.colorPrimary));
                mText_tabContact.setTextColor(getResources().getColor(R.color.text_gray));
                mText_tabMe.setTextColor(getResources().getColor(R.color.text_gray));

                mViewPager.setCurrentItem(0, true);
                break;
            case 1:
                mTitleName.setText("联系人");
                mLoading.setVisibility(View.GONE);
                mBt_Add.setVisibility(View.VISIBLE);

                mImage_tabMessage.setImageResource(R.drawable.tab_message_0);
                mImage_tabContact.setImageResource(R.drawable.tab_contact_1);
                mImage_tabMe.setImageResource(R.drawable.tab_me_0);

                mText_tabMessage.setTextColor(getResources().getColor(R.color.text_gray));
                mText_tabContact.setTextColor(getResources().getColor(R.color.colorPrimary));
                mText_tabMe.setTextColor(getResources().getColor(R.color.text_gray));

                mViewPager.setCurrentItem(1, true);
                break;
            case 2:
                mLoading.setVisibility(View.GONE);
                mBt_Add.setVisibility(View.GONE);
                mTitleName.setText("我");

                mImage_tabMessage.setImageResource(R.drawable.tab_message_0);
                mImage_tabContact.setImageResource(R.drawable.tab_contact_0);
                mImage_tabMe.setImageResource(R.drawable.tab_me_1);

                mText_tabMessage.setTextColor(getResources().getColor(R.color.text_gray));
                mText_tabContact.setTextColor(getResources().getColor(R.color.text_gray));
                mText_tabMe.setTextColor(getResources().getColor(R.color.colorPrimary));

                mViewPager.setCurrentItem(2, true);
                break;
            default:
                break;
        }
    }

    private void showPopupWindow() {
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(mPopupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            //如果不设置BackgroundDrawable的话setOutsideTouchable(true)方法无效,调用setOutsideTouchable(true)就必须调用setFocusable(true)
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            mPopupWindow.showAsDropDown(mPopupWindowLocationView);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return true;
        } else {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                moveTaskToBack(false);
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onUpdate(int function, Object data) {
        //需要刷新消息页签的最后一条消息内容就表示已经发送过消息 如果当前主界面的页签不是消息页签就用打开消息页签
        if (NotifyReceiver.NOTIFY_TYPE_UPDATE_MESSAGE_PREVIEW_ITEM_CONTENT == function) {
            if (mViewPager.getCurrentItem() != 0) {
                selectTab(0);
            }
        }
        //刷新消息页签底部tab未读数=每个item未读数相加
        if (function == NotifyReceiver.NOTIFY_TYPE_UPDATE_MESSAGE_PREVIEW_TAB_NOT_READ) {
            if (data != null) {
                Intent intent = (Intent) data;
                String notReadNum = intent.getStringExtra("notReadNum");
                notReadNum = TextUtils.isEmpty(notReadNum) ? "0" : notReadNum;
                if (!"0".equals(notReadNum)) {
                    mNotRead_tabMessage.setVisibility(View.VISIBLE);
                    mNotRead_tabMessage.setText(notReadNum);
                } else {
                    mNotRead_tabMessage.setVisibility(View.GONE);
                }
            }
        }
    }
}

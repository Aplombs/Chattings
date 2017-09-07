package com.chat.im.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.fragment.ContactFragment;
import com.chat.im.fragment.MeFragment;
import com.chat.im.fragment.MessageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private ImageView mImage_tabMessage, mImage_tabContact, mImage_tabMe;
    private View mLoading, mTip_me, mTabMessage, mTabContact, mTabMe;
    private TextView mText_tabMessage, mText_tabContact, mText_tabMe, mNotRead_tabMessage, mNotRead_tabContact;

    @Override
    protected void init() {
        mBt_Add.setVisibility(View.VISIBLE);
        mReturnView.setVisibility(View.GONE);

        mLoading = findViewById(R.id.title_bar_loading);

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
        mNotRead_tabContact = (TextView) findViewById(R.id.not_read_tab_contact);
        mTip_me = findViewById(R.id.tip_tab_me);

        mViewPager = (ViewPager) findViewById(R.id.viewpager_main);

        mTabMessage.setOnClickListener(this);
        mTabContact.setOnClickListener(this);
        mTabMe.setOnClickListener(this);

        initViewPager();

        mViewPager.setCurrentItem(0);
        selectTab(0);
    }

    private void initViewPager() {
        MessageFragment messageFragment = new MessageFragment();
        ContactFragment contactFragment = new ContactFragment();
        MeFragment meFragment = new MeFragment();

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
            case R.id.tab_message_main:
                selectTab(0);
                break;
            case R.id.tab_contact_main:
                selectTab(1);
                break;
            case R.id.tab_me_main:
                selectTab(2);
                break;
        }
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
                mText_tabContact.setTextColor(getResources().getColor(R.color.gray));
                mText_tabMe.setTextColor(getResources().getColor(R.color.gray));

                mViewPager.setCurrentItem(0);
                break;
            case 1:
                mTitleName.setText("联系人");
                mLoading.setVisibility(View.GONE);
                mBt_Add.setVisibility(View.VISIBLE);

                mImage_tabMessage.setImageResource(R.drawable.tab_message_0);
                mImage_tabContact.setImageResource(R.drawable.tab_contact_1);
                mImage_tabMe.setImageResource(R.drawable.tab_me_0);

                mText_tabMessage.setTextColor(getResources().getColor(R.color.gray));
                mText_tabContact.setTextColor(getResources().getColor(R.color.colorPrimary));
                mText_tabMe.setTextColor(getResources().getColor(R.color.gray));

                mViewPager.setCurrentItem(1);
                break;
            case 2:
                mLoading.setVisibility(View.GONE);
                mBt_Add.setVisibility(View.GONE);
                mTitleName.setText("我");

                mImage_tabMessage.setImageResource(R.drawable.tab_message_0);
                mImage_tabContact.setImageResource(R.drawable.tab_contact_0);
                mImage_tabMe.setImageResource(R.drawable.tab_me_1);

                mText_tabMessage.setTextColor(getResources().getColor(R.color.gray));
                mText_tabContact.setTextColor(getResources().getColor(R.color.gray));
                mText_tabMe.setTextColor(getResources().getColor(R.color.colorPrimary));

                mViewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

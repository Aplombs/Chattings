package com.chat.im.test;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.test.fragment.NewsFragmentTouTiao;
import com.chat.im.test.fragment.NewsFragmentTouTiaoSmartRefresh;
import com.chat.im.test.fragment.NewsFragmentWeibo;
import com.chat.im.ui.BaseActivity;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.jetbrains.anko.ContextUtilsKt.getDisplayMetrics;

/**
 * 资讯
 *
 * @author kfzx-tanglitao
 */

public class TestNewsActivity extends BaseActivity {

    @BindView(R.id.tab_layout__test_news)
    TabLayout mNewsTabLayout;

    @BindView(R.id.viewpager_test_news)
    ViewPager mNewsViewPager;

    private NewsPagerAdapter mNewsPagerAdapter;


    private String[] tabTitles = {"消息", "资讯", "公众号"};
    private Fragment[] fragments = {new NewsFragmentWeibo(), new NewsFragmentTouTiao(), new NewsFragmentTouTiaoSmartRefresh()};

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_test_news;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);

        mTitleName.setText("资讯");
        mBt_Add.setVisibility(View.GONE);
        mReturnView.setVisibility(View.VISIBLE);

        mNewsPagerAdapter = new NewsPagerAdapter(getSupportFragmentManager());
        mNewsViewPager.setAdapter(mNewsPagerAdapter);
        mNewsTabLayout.setupWithViewPager(mNewsViewPager);

        setIndicator(this, mNewsTabLayout, 10, 10);

        for (int i = 0; i < mNewsTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mNewsTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(mNewsPagerAdapter.getTabView(i));
            }
        }

        mNewsViewPager.setCurrentItem(1);

        mNewsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (view != null) {
                    mNewsPagerAdapter.setTabStatus(view, tab.getPosition(), true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (view != null) {
                    mNewsPagerAdapter.setTabStatus(view, tab.getPosition(), false);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private class NewsPagerAdapter extends FragmentPagerAdapter {

        public NewsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

//        @Nullable
//        @Override
//        public CharSequence getPageTitle(int position) {
//            if (1 == position) {
//                Drawable drawable = getResources().getDrawable(images[position]);
//                drawable.setBounds(0, 0, 50, 50);
//                // Replace blank spaces with image icon
//                SpannableString sb = new SpannableString("   " + tabTitles[position]);
//                ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
//                sb.setSpan(imageSpan, 1, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                return sb;
//            } else {
//                return tabTitles[position];
//            }
//        }

        public View getTabView(int position) {
            View view = LayoutInflater.from(TestNewsActivity.this).inflate(R.layout.item_tab_layout_test_news, null);
            if (position == 1) {
                setTabStatus(view, position, true);
            } else {
                setTabStatus(view, position, false);
            }
            return view;
        }

        public void setTabStatus(View view, int position, boolean isSelect) {
            TextView title = view.findViewById(R.id.tv_title_tab_layout_test_news);
            ImageView titleIcon = view.findViewById(R.id.iv_title_icon_tab_layout_test_news);

            boolean isNewsTab = position == 1;

            if (isSelect) {
                title.setTextSize(16);
                title.setTextColor(getResources().getColor(R.color.white));
                if (isNewsTab) {
                    titleIcon.setVisibility(View.VISIBLE);
                    titleIcon.setImageResource(R.drawable.icon_arrow_1);
                } else {
                    titleIcon.setVisibility(View.GONE);
                }
            } else {
                title.setTextSize(14);
                title.setTextColor(Color.parseColor("#C8C8C8"));

                if (isNewsTab) {
                    titleIcon.setVisibility(View.VISIBLE);
                    titleIcon.setImageResource(R.drawable.icon_arrow_0);
                } else {
                    titleIcon.setVisibility(View.GONE);
                }
            }

            title.setText(tabTitles[position]);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }

    /**
     * 通过反射修改TabLayout Indicator的宽度（仅在Android 4.2及以上生效）
     */
    private void setIndicator(Context context, TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout ll_tab = null;
        try {
            ll_tab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) (getDisplayMetrics(context).density * leftDip);
        int right = (int) (getDisplayMetrics(context).density * rightDip);

        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
package com.chat.im.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.helper.UIHelper;
import com.chat.im.helper.UtilsHelper;

/**
 * Activity基类
 * 注意
 * 1 子类布局res需<include layout="@layout/title_bar" />
 */

public abstract class BaseActivity extends FragmentActivity {

    //右上角+
    private ImageView mBt_Add;
    //顶部标题
    private TextView mTitleName;
    //左上角返回按钮
    private View mReturnView;
    //点击返回键时间判断
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setCurrentLayoutRes());

        //沉浸式状态栏
        setTranslucentStatusBar();

        mTitleName = (TextView) findViewById(R.id.title_bar_title);
        mBt_Add = (ImageView) findViewById(R.id.title_bar_add);
        mReturnView = findViewById(R.id.ll_return_top);

        mTitleName.setText(setCurrentTitle());
        mBt_Add.setVisibility(setRightButtonVisibility());
        mReturnView.setVisibility(setLeftReturnButtonVisibility());

        mReturnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.super.onBackPressed();
            }
        });

        init();
    }

    //状态栏透明处理--很多人都叫沉浸式状态栏
    private void setTranslucentStatusBar() {
        Window mWindow = getWindow();
        //android 6.0以上设置状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //防止系统栏隐藏时内容区域大小发生变化
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            //Activity全屏显示,但状态栏不会被隐藏覆盖,状态栏依然可见,Activity顶端布局部分会被状态栏遮住
            uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个才能设置状态栏颜色
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            mWindow.setStatusBarColor(getResources().getColor(R.color.gray));
            //解决状态栏和自己的titleBar中间有条水印线
            mWindow.getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            // 初始化5.0以下，4.4以上沉浸式
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorViewGroup = (ViewGroup) mWindow.getDecorView();
            //创建一个宽高大小和statusBar一样的view,颜色设置成Toolbar或者ActionBar保持一致,然后添加到decorView
            View mStatusBarTintView = new View(this);
            //获取系统statusBar高度
            int statusBarHeight = UtilsHelper.getInstance().getStatusBarHeight(getResources());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
            params.gravity = Gravity.TOP;
            mStatusBarTintView.setLayoutParams(params);
            //设置想要的颜色
            mStatusBarTintView.setBackgroundColor(getResources().getColor(R.color.gray));
            mStatusBarTintView.setVisibility(View.VISIBLE);
            decorViewGroup.addView(mStatusBarTintView);
        }
    }

    /**
     * 初始化操作
     */
    protected abstract void init();

    /**
     * 设置当前Activity布局的res
     *
     * @return 当前布局的resID
     */
    protected abstract int setCurrentLayoutRes();

    /**
     * View.GONE;
     * View.VISIBLE;
     *
     * @return 返回按钮是否可见
     */
    protected abstract int setLeftReturnButtonVisibility();

    /**
     * View.GONE;
     * View.VISIBLE;
     *
     * @return 右上角+是否可见
     */
    protected abstract int setRightButtonVisibility();

    /**
     * @return 当前界面标题
     */
    protected abstract CharSequence setCurrentTitle();

    /**
     * @return 是否需要按两次返回(子类处理)
     */
    protected abstract boolean isNeedTwiceToReturn();

    @Override
    public void onBackPressed() {
        if (isNeedTwiceToReturn()) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                UIHelper.getInstance().toast("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

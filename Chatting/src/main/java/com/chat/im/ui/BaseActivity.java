package com.chat.im.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.helper.UtilsHelper;

/**
 * Activity基类
 * 注意
 * 1 子类布局res需<include layout="@layout/title_bar" />
 * 2 init()添加以下代码
 * mTitleName.setText("title");
 * mBt_Add.setVisibility(View.GONE);
 * mReturnView.setVisibility(View.VISIBLE);
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Context mContext = this;
    //右上角+
    protected ImageView mBt_Add;
    protected ImageView mBt_More;
    //顶部标题
    protected TextView mTitleName;
    //左上角返回按钮
    protected View mReturnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutRes());

        //沉浸式状态栏
        setTranslucentStatusBar();

        mTitleName = (TextView) findViewById(R.id.title_bar_title);
        mBt_Add = (ImageView) findViewById(R.id.title_bar_add);
        mBt_More = (ImageView) findViewById(R.id.title_bar_more);
        mReturnView = findViewById(R.id.ll_return_top);

        mReturnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.super.onBackPressed();
            }
        });

        init();
    }

    /**
     * 状态栏透明处理--很多人都叫沉浸式状态栏
     */
    private void setTranslucentStatusBar() {
        Window mWindow = getWindow();
        //android 6.0以上设置状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //需要设置这个才能设置状态栏颜色
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            mWindow.setStatusBarColor(getResources().getColor(R.color.title_bar_gray));

            //让应用的主体内容占用系统状态栏的空间(将布局内容延伸到状态栏),应用主题theme需要使用AppCompat类的
            //int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            //mWindow.getDecorView().setSystemUiVisibility(uiFlags);
            //mWindow.setStatusBarColor(Color.TRANSPARENT);
            //也可在布局中使用view(可以是图片)占位状态栏位置,最后动态设置view高度
            //View mStatusBarTintView = findViewById(R.id.status_height);
            //int statusBarHeight = getStatusBarHeight(getResources());
            //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
            //params.gravity = Gravity.TOP;
            //mStatusBarTintView.setLayoutParams(params);
        } else {
            // 初始化5.0以下，4.4以上沉浸式
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorViewGroup = (ViewGroup) mWindow.getDecorView();
            //创建一个宽高大小和statusBar一样的view,颜色设置成自己的titleBar或者Toolbar或者ActionBar保持一致,然后添加到decorView
            View mStatusBarTintView = new View(this);
            //获取系统statusBar高度
            int statusBarHeight = UtilsHelper.getInstance().getStatusBarHeight(getResources());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
            params.gravity = Gravity.TOP;
            mStatusBarTintView.setLayoutParams(params);
            //设置想要的颜色
            mStatusBarTintView.setBackgroundColor(getResources().getColor(R.color.title_bar_gray));
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
    protected abstract int setLayoutRes();
}

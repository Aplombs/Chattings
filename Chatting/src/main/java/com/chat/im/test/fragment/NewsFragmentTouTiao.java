package com.chat.im.test.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.helper.LogHelper;
import com.chat.im.helper.UIHelper;
import com.chat.im.test.adapter.NewsImageTextRecyclerViewAdapter;
import com.chat.im.test.jsonbean.NewsImageText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 资讯 - 头条的下拉刷新效果
 *
 * @author kfzx-tanglitao on 2017/11/15.
 */

public class NewsFragmentTouTiao extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    //刷新之后顶部提示view
    @Bind(R.id.view_refreshTip_news)
    TextView mRefreshTipView;
    //图文消息RecyclerView
    @Bind(R.id.recyclerView_imageText_news)
    RecyclerView mRecyclerViewImageText;
    //下拉刷新控件
    @Bind(R.id.bgaRefresh_layout_news)
    BGARefreshLayout mRefreshLayout;
    //整个布局的根布局控件
    @Bind(R.id.root_layout_news)
    LinearLayout rootLayout;
    /**
     * 图文集合数据
     */
    private List<NewsImageText> mImageTextList = new ArrayList<>();

    private int mNewPageNumber;
    private Handler mHandler = new Handler();
    private boolean mRefreshTipViewIsShowing = false;
    private final String TAG = "[NewsFragmentTouTiao] ";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test_news_toutiao, null);
        ButterKnife.bind(this, view);
        //初始化BGARefresh下拉刷新控件
        initBGARefreshLayout();
        //初始化图文RecyclerView
        initImageTextRecyclerView();

        return view;
    }

    private void initBGARefreshLayout() {
        // 为BGARefreshLayout设置监听
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格
        BGANormalRefreshViewHolder normalRefreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        // 设置顶部刷新控件多少时间完成下拉动画
        normalRefreshViewHolder.setTopAnimDuration(300);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(normalRefreshViewHolder);
    }

    private void initImageTextRecyclerView() {
        initFalseData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewImageText.setLayoutManager(linearLayoutManager);
        NewsImageTextRecyclerViewAdapter mAdapter = new NewsImageTextRecyclerViewAdapter(getActivity(), mImageTextList);
        mRecyclerViewImageText.setAdapter(mAdapter);
    }

    /**初始化本地假数据*/
    private void initFalseData() {
        NewsImageText imageText;
        for (int i = 0; i < 15; i++) {
            imageText = new NewsImageText();
            if (i == 0) {
                imageText.setItemViewType(100);
            }
            mImageTextList.add(imageText);
        }
    }

    //下拉刷新正在刷新中...
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mNewPageNumber++;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.endRefreshing();
                if (mNewPageNumber > 10) {
                    show("没有最新数据了");
                    return;
                }

                show("融e联为您更新10条内容");
            }
        }, 1000);
    }

    //上拉加载更多正在刷新中...
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mNewPageNumber++;
        //模拟请求网络
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.endLoadingMore();
                if (mNewPageNumber > 10) {
                    UIHelper.getInstance().toast("没有最新数据了");
                    return;
                }
                UIHelper.getInstance().toast("融e联为您更新10条内容");
            }
        }, 1000);

        return true;
    }

    private void show(String tipContent) {
        if (mRefreshTipViewIsShowing) {
            return;
        }

        mRefreshTipViewIsShowing = true;

        if (!TextUtils.isEmpty(tipContent)) {
            mRefreshTipView.setText(tipContent);
        }

        float parseFloat = Float.parseFloat(-mRefreshTipView.getHeight() + "");
        LogHelper.e(TAG + "show() mRefreshTipView:" + parseFloat);
        // 下拉刷新步骤
        // 给mTipView做弹出动画(其实是最先执行的是将rootLayout平移到初始位置),展示一段时间;
        // 将mTipView假象的给平移到屏幕之外(因为是假象所以每次最先执行rootLayout的平移操作),其实是给整个rootLayout做平移动画,动画结束之后将mTipView设置inVisible(千万不能设置gone,设置了会从新绘制rootLayout,界面会向上错位,错位的距离为MTipView的高度)

        // 由于上一次下拉刷新之后将rootLayout向上平移了mTipView的高度,所以再次下拉之前先将整个rootLayout平移回到初始的位置;
        // 第一次平移的情况:由于mTipView初始是gone,所以第一次平移的距离就是0
        // 第二次平移的情况:由于已经下拉过了此时的没TipView的状态是inVisible,所以第二次平移的距离就是mTipView的Height
        ObjectAnimator translationYToInitPosition = ObjectAnimator.ofFloat(rootLayout, "translationY", parseFloat, 0f);
        translationYToInitPosition.setDuration(0);
        //先将rootLayout平移到初始位置
        translationYToInitPosition.start();

        translationYToInitPosition.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                showTipView();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }

    private void showTipView() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mRefreshTipView, "scaleX", 0, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mRefreshTipView, "scaleY", 0, 1f);

        animatorSet.setDuration(500);
        animatorSet.play(scaleX).with(scaleY);
        animatorSet.start();

        mRefreshTipView.setVisibility(View.VISIBLE);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideTipView();
                    }
                    //mTipView悬停时间
                }, 1200);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void hideTipView() {
        //ABSOLUTE	            构造中的参数，绝对坐标
        //RELATIVE_TO_PARENT	构造中的参数，相对于父控件身的坐标
        //RELATIVE_TO_SELF  	构造中的参数，相对于自身的坐标
        int fromXType = Animation.RELATIVE_TO_SELF;
        int toXType = Animation.RELATIVE_TO_SELF;
        int fromYType = Animation.RELATIVE_TO_SELF;
        int toYType = Animation.RELATIVE_TO_SELF;

        ObjectAnimator translationY = ObjectAnimator.ofFloat(rootLayout, "translationY", 0f, Float.parseFloat(-mRefreshTipView.getHeight() + ""));
        translationY.setDuration(500);
        translationY.start();

        translationY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRefreshTipView.setVisibility(View.INVISIBLE);
                mRefreshTipViewIsShowing = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
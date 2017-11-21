package com.chat.im.test.fragment;

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
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.test.adapter.NewsImageTextRecyclerViewAdapter;
import com.chat.im.test.jsonbean.NewsImageText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 资讯 - 微博的下拉刷新效果
 *
 * @author kfzx-tanglitao on 2017/11/15.
 */

public class NewsFragmentWeibo extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @BindView(R.id.view_refreshTip_news)
    TextView mTipView;
    @BindView(R.id.recyclerView_imageText_news)
    RecyclerView mRecyclerView;
    @BindView(R.id.bgaRefresh_layout_news)
    BGARefreshLayout mRefreshLayout;
    /**
     * 图文集合数据
     */
    private List<NewsImageText> mImageTextList = new ArrayList<>();


    private Handler mHandler = new Handler();
    private NewsImageTextRecyclerViewAdapter mAdapter;
    private int mNewPageNumber;
    private Runnable showTipTask = new Runnable() {
        @Override
        public void run() {
            if (mNewPageNumber > 10) {
                showTopTipView("没有最新数据了");
                return;
            }

            showTopTipView("融e联为您更新10条内容");
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_news_weibo, null);
        ButterKnife.bind(this, view);

        // 为BGARefreshLayout 设置代理
        mRefreshLayout.setDelegate(this);

        // 设置下拉刷新和上拉加载更多的风格
        BGANormalRefreshViewHolder normalRefreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(normalRefreshViewHolder);

        initFalseData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new NewsImageTextRecyclerViewAdapter(getActivity(), mImageTextList);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    /**
     * 初始化本地假数据
     */
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

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

        mNewPageNumber++;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.endRefreshing();
                mHandler.postDelayed(showTipTask, 400);
            }
        }, 1000);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {

        mNewPageNumber++;
        //模拟请求网络
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.endLoadingMore();
                mHandler.postDelayed(showTipTask, 400);
            }
        }, 1000);

        return true;
    }

    private void showTopTipView(String tipContent) {

        if (!TextUtils.isEmpty(tipContent)) {
            mTipView.setText(tipContent);
        }
        mTipView.setVisibility(View.VISIBLE);

        final TranslateAnimation hideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF
                , -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        //设置动画执行完所需时间
        hideAnim.setDuration(500);
        mTipView.startAnimation(hideAnim);

        hideAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hide();
                    }
                }, 1500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

//        AnimatorSet animatorSet = new AnimatorSet();
//        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mTipViewLayout, "scaleX", 0, 1f);
//        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mTipViewLayout, "scaleY", 0, 1f);
//
//        animatorSet.setDuration(500);
//        animatorSet.play(scaleX).with(scaleY);
//        animatorSet.start();
//
//        animatorSet.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        hide();
//                    }
//                }, 1500);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
    }

    private void hide() {
        final TranslateAnimation hideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF
                , 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);

        hideAnim.setDuration(500);
        mTipView.startAnimation(hideAnim);

        hideAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTipView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
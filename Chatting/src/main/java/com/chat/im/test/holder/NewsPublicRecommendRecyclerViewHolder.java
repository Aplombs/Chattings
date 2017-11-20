package com.chat.im.test.holder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chat.im.R;
import com.chat.im.helper.ContextHelper;
import com.chat.im.test.adapter.NewsPublicRecommendRecyclerViewAdapter;

/**
 * 热门推荐
 *
 * @author kfzx-tanglitao 2017/11/20
 */

public class NewsPublicRecommendRecyclerViewHolder extends BaseRecyclerViewHolder {

    RecyclerView mRecyclerViewPublicRecommend;

    public NewsPublicRecommendRecyclerViewHolder(View itemView) {
        super(itemView);
        mRecyclerViewPublicRecommend = itemView.findViewById(R.id.recyclerView_publicAccountRecommend_item_news);
    }

    @Override
    public void onBindViewHolder() {
        Context context = ContextHelper.getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        //设置公众号热门推荐横向水平滑动
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewPublicRecommend.setLayoutManager(linearLayoutManager);
        NewsPublicRecommendRecyclerViewAdapter mAdapter = new NewsPublicRecommendRecyclerViewAdapter(context);
        mRecyclerViewPublicRecommend.setAdapter(mAdapter);
    }
}
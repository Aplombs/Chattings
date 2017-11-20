package com.chat.im.test.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chat.im.R;

import butterknife.ButterKnife;

/**
 * 发现-资讯页签 公众号热门推荐RecyclerViewAdapter
 *
 * @author kfzx-tanglitao on 2017/11/17.
 */

public class NewsPublicRecommendRecyclerViewAdapter extends RecyclerView.Adapter<NewsPublicRecommendRecyclerViewAdapter.NewsPublicRecommendRecyclerViewHolder> {

    private Context mContext;

    public NewsPublicRecommendRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public NewsPublicRecommendRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.itemview_public_recommend_news, parent, false);
        return new NewsPublicRecommendRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsPublicRecommendRecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class NewsPublicRecommendRecyclerViewHolder extends RecyclerView.ViewHolder {

        public NewsPublicRecommendRecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

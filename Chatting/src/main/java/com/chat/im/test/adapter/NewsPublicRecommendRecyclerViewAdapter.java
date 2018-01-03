package com.chat.im.test.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.chat.im.test.holder.BaseRecyclerViewHolderFactory;

/**
 * 发现-资讯页签 公众号热门推荐RecyclerViewAdapter
 *
 * @author kfzx-tanglitao on 2017/11/17.
 */

public class NewsPublicRecommendRecyclerViewAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolderFactory> {

    private Context mContext;

    public NewsPublicRecommendRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        return 101;
    }

    @Override
    public BaseRecyclerViewHolderFactory onCreateViewHolder(ViewGroup parent, int viewType) {
        return BaseRecyclerViewHolderFactory.onCreateViewHolder(mContext, parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolderFactory holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}

package com.chat.im.test.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.chat.im.test.holder.BaseRecyclerViewHolder;
import com.chat.im.test.jsonbean.NewsImageText;

import java.util.List;

/**
 * 发现-资讯页签 图文RecyclerViewAdapter
 *
 * @author kfzx-tanglitao on 2017/11/17.
 */

public class NewsImageTextRecyclerViewAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private Context mContext;
    private List<NewsImageText> mList;

    public NewsImageTextRecyclerViewAdapter(Context mContext, List<NewsImageText> imageTextList) {
        this.mContext = mContext;
        this.mList = imageTextList;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getItemViewType();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BaseRecyclerViewHolder.onCreateViewHolder(mContext, parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.onBindViewHolder();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}

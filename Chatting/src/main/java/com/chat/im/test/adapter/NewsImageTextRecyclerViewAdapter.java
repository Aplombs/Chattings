package com.chat.im.test.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.test.jsonbean.NewsImageText;

import java.util.List;

/**
 * 发现-资讯页签 图文RecyclerViewAdapter
 *
 * @author kfzx-tanglitao on 2017/11/17.
 */

public class NewsImageTextRecyclerViewAdapter extends RecyclerView.Adapter<NewsImageTextRecyclerViewAdapter.NewsImageTextRecyclerViewHolder> {

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
    public NewsImageTextRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 100) {
            view = LayoutInflater.from(mContext).inflate(R.layout.itemview_image_text_news_recyclerview, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.itemview_image_text_news, parent, false);
        }
        return new NewsImageTextRecyclerViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(NewsImageTextRecyclerViewHolder holder, int position) {
        if (0 == position) {
            //初始化公众号热门推荐RecyclerView
            initPublicRecommendRecyclerView(holder);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class NewsImageTextRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView itemImageText;
        RecyclerView recyclerViewPublicRecommend;

        public NewsImageTextRecyclerViewHolder(View itemView, int viewType) {
            super(itemView);
            if (100 == viewType) {
                recyclerViewPublicRecommend = itemView.findViewById(R.id.recyclerView_publicAccountRecommend_item_news);
            } else {
                itemImageText = itemView.findViewById(R.id.item_imageText);
            }
        }
    }

    private void initPublicRecommendRecyclerView(NewsImageTextRecyclerViewHolder holder) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        //设置公众号热门推荐横向水平滑动
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerViewPublicRecommend.setLayoutManager(linearLayoutManager);
        NewsPublicRecommendRecyclerViewAdapter mAdapter = new NewsPublicRecommendRecyclerViewAdapter(mContext);
        holder.recyclerViewPublicRecommend.setAdapter(mAdapter);
    }
}

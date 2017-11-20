package com.chat.im.test.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chat.im.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 发现-资讯页签 图文RecyclerViewAdapter
 *
 * @author kfzx-tanglitao on 2017/11/17.
 */

public class NewsImageTextRecyclerViewAdapter extends RecyclerView.Adapter<NewsImageTextRecyclerViewAdapter.NewsImageTextRecyclerViewHolder> {

    private Context mContext;

    public NewsImageTextRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public NewsImageTextRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.itemview_image_text_news, parent, false);
        return new NewsImageTextRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsImageTextRecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    class NewsImageTextRecyclerViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_imageText)
        TextView itemImageText;

        public NewsImageTextRecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

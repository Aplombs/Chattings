package com.chat.im.test.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chat.im.R;

/**
 * RecyclerView的viewHolder基类
 *
 * @author kfzx-tanglitao
 */

public abstract class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    protected static Context mContext;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public static BaseRecyclerViewHolder onCreateViewHolder(Context context, ViewGroup parent, int viewType) {
        mContext = context;

        BaseRecyclerViewHolder baseRecyclerViewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;
        switch (viewType) {
            case 99:
                itemView = inflater.inflate(R.layout.itemview_image_text_news, parent, false);
                baseRecyclerViewHolder = new NewsImageTextRecyclerViewHolder(itemView);
                break;
            case 100:
                itemView = inflater.inflate(R.layout.itemview_publice_recommend_news, parent, false);
                baseRecyclerViewHolder = new NewsPublicRecommendRecyclerViewHolder(itemView);
                break;
            default:
                break;
        }
        return baseRecyclerViewHolder;
    }

    /**
     *
     */
    public abstract void onBindViewHolder();

    public static void clearCache() {
        mContext = null;
    }
}

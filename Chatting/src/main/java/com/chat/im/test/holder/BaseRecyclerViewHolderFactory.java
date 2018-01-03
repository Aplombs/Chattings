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

public abstract class BaseRecyclerViewHolderFactory extends RecyclerView.ViewHolder {

    protected static Context mContext;

    public BaseRecyclerViewHolderFactory(View itemView) {
        super(itemView);
    }

    public static BaseRecyclerViewHolderFactory onCreateViewHolder(Context context, ViewGroup parent, int viewType) {
        mContext = context;

        BaseRecyclerViewHolderFactory baseRecyclerViewHolderFactory = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;
        switch (viewType) {
            case 99:
                itemView = inflater.inflate(R.layout.itemview_image_text_news, parent, false);
                baseRecyclerViewHolderFactory = new NewsImageTextRecyclerViewHolderFactory(itemView);
                break;
            case 100:
                itemView = inflater.inflate(R.layout.itemview_publice_recommend_news, parent, false);
                baseRecyclerViewHolderFactory = new NewsPublicRecommendRecyclerViewHolderFactory(itemView);
                break;
            case 101:
                itemView = inflater.inflate(R.layout.itemview_public_recommend_news, parent, false);
                baseRecyclerViewHolderFactory = new NewsPublicRecommendRecyclerViewHolderFactory(itemView);
                break;
            default:
                break;
        }
        return baseRecyclerViewHolderFactory;
    }

    /**
     *
     */
    public abstract void onBindViewHolder();

    public static void clearCache() {
        mContext = null;
    }
}

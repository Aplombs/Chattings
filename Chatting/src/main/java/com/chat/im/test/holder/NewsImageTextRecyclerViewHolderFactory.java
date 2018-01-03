package com.chat.im.test.holder;

import android.view.View;
import android.widget.TextView;

import com.chat.im.R;

/**
 * 资讯图文
 *
 * @author kfzx-tanglitao 2017/11/20
 */

public class NewsImageTextRecyclerViewHolderFactory extends BaseRecyclerViewHolderFactory {

    TextView itemImageText;

    public NewsImageTextRecyclerViewHolderFactory(View itemView) {
        super(itemView);
        itemImageText = itemView.findViewById(R.id.item_imageText);
    }

    @Override
    public void onBindViewHolder() {

    }
}

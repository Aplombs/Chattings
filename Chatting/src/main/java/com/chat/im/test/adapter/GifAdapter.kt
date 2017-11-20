package com.chat.im.test.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chat.im.R
import com.chat.im.test.jsonbean.Gif
import org.jetbrains.anko.find

/**
 *GIF的RecyclerView的Adapter
 */
class GifAdapter(private val mContext: Context, private val mList: List<Gif>) : RecyclerView.Adapter<GifAdapter.GifViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GifViewHolder {
        return GifViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_gif_test_kotlin, parent, false))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: GifViewHolder?, position: Int) {
        val gif = mList[position]
        Glide.with(mContext).asBitmap().load(gif.img).into(holder?.image)
    }

    class GifViewHolder(item_view: View) : RecyclerView.ViewHolder(item_view) {
        val image: ImageView = item_view.find(id = R.id.gif_image)
    }
}
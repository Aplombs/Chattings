package com.chat.im.test.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chat.im.R
import com.chat.im.test.jsonbean.JokeContent
import org.jetbrains.anko.find

/**
段子的RecyclerView的Adapter
 */
class JokeAdapter(private val mContext: Context, private val mList: List<JokeContent>) : RecyclerView.Adapter<JokeAdapter.JokeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): JokeViewHolder {
        return JokeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_joke_test_kotlin, parent, false))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: JokeViewHolder?, position: Int) {
        val jokeContent = mList[position]
        holder?.title?.text = jokeContent.title
        holder?.content?.text = jokeContent.text
    }

    class JokeViewHolder(item_view: View) : RecyclerView.ViewHolder(item_view) {
        val title: TextView = item_view.find(id = R.id.joke_title)
        val content: TextView = item_view.find(id = R.id.joke_content)
    }
}
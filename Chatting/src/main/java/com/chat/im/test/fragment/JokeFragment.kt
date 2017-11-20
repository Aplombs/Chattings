package com.chat.im.test.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chat.im.R
import com.chat.im.test.adapter.JokeAdapter
import com.chat.im.test.http.HttpRequestHelper
import com.chat.im.test.jsonbean.JokeContent
import kotlinx.android.synthetic.main.fragment_test_kotlin_joke.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


class JokeFragment : Fragment() {

    //当前加载页数
    private var mPageIndex: Int = 1
    private val httpHelper = HttpRequestHelper()
    private var mJokeList: MutableList<JokeContent> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_test_kotlin_joke, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView_joke_test_kotlin?.layoutManager = LinearLayoutManager(context)
        recyclerView_joke_test_kotlin?.adapter = JokeAdapter(context!!, mJokeList)

        swipeRefreshLayout_joke_test_kotlin?.setColorSchemeResources(R.color.colorPrimary)

        swipeRefreshLayout_joke_test_kotlin?.setOnRefreshListener {
            mPageIndex += 1
            //loadData()
        }

        //loadData()
    }

    private fun loadData() {
        swipeRefreshLayout_joke_test_kotlin?.isRefreshing = true
        doAsync {
            val jokeData = httpHelper.getJokeData(mPageIndex, 1)
            mJokeList.addAll(0, jokeData!!)
            uiThread {
                swipeRefreshLayout_joke_test_kotlin?.isRefreshing = false

                recyclerView_joke_test_kotlin?.adapter?.notifyItemInserted(0)
                recyclerView_joke_test_kotlin?.smoothScrollToPosition(0)
            }
        }
    }
}
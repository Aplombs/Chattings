package com.chat.im.test.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chat.im.R
import com.chat.im.test.adapter.GifAdapter
import com.chat.im.test.http.HttpRequestHelper
import com.chat.im.test.jsonbean.Gif
import kotlinx.android.synthetic.main.fragment_test_kotlin_gif.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class GifFragment : Fragment() {

    //当前加载页数
    private var mPageIndex: Int = 1
    private val httpHelper = HttpRequestHelper()
    private var mGifList: MutableList<Gif> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_test_kotlin_gif, null)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView_gif_test_kotlin?.layoutManager = LinearLayoutManager(context)
        recyclerView_gif_test_kotlin?.adapter = GifAdapter(context!!, mGifList)

        swipeRefreshLayout_gif_test_kotlin?.setColorSchemeResources(R.color.colorPrimary)

        swipeRefreshLayout_gif_test_kotlin?.setOnRefreshListener {
            mPageIndex += 1
            loadData()
        }

        loadData()
    }

    private fun loadData() {
        swipeRefreshLayout_gif_test_kotlin?.isRefreshing = true
        doAsync {
            val gifData = httpHelper.getGIFData(mPageIndex, 1)
            mGifList.addAll(0, gifData)
            uiThread {
                swipeRefreshLayout_gif_test_kotlin?.isRefreshing = false

                recyclerView_gif_test_kotlin?.adapter?.notifyItemInserted(0)
                recyclerView_gif_test_kotlin?.smoothScrollToPosition(0)
            }
        }
    }
}
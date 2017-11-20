package com.chat.im.test.http

import com.chat.im.helper.JsonHelper
import com.chat.im.helper.LogHelper
import com.chat.im.test.jsonbean.*
import java.lang.Exception
import java.net.URL

/**
http网络请求帮助类
 */
class HttpRequestHelper {

    private val appID = "45578"
    private val secret = "4e6e7a13e16a42059a75a9a8931a779f"
    private val baseURL = "&showapi_sign=$secret&showapi_appid=$appID"

    private val TAG: String = "[HttpRequestHelper] "
    //段子 maxPageIndex=10
    private val jokeBaseUrl = "http://route.showapi.com/341-1"
    //励志鸡汤 noMaxPageIndex
    private val jokeBaseUrl2 = "http://route.showapi.com/1211-1"
    //GIF maxPageIndex=5
    private val gifBaseUrl = "http://route.showapi.com/341-3"
    //美女 maxPageIndex=20 type
    //("大胸妹", "小清新", "文艺范", "性感妹", "大长腿", "黑丝袜", "小翘臀")
    //(34, 35, 36, 37, 38, 39, 40)
    private val beautyGirlUrl = "http://route.showapi.com/819-1"

    private fun buildUrl(url: String): String {
        return "$url&$baseURL"
    }

    fun getJokeData(pageIndex: Int, maxResult: Int = 10): List<JokeContent>? {
        var jokeList: List<JokeContent>? = null
        try {
            val url = buildUrl("$jokeBaseUrl?page=$pageIndex&maxResult=$maxResult")
            LogHelper.e(TAG + "getJokeUrl:$url")
            val readResult = URL(url).readText()
            jokeList = JsonHelper.gsonToBean(readResult, JokeText::class.java).showapi_res_body.contentlist
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return jokeList
    }

    fun getGIFData(pageIndex: Int, maxResult: Int = 5): List<Gif> {
        var gifList: List<Gif>? = null
        try {
            val url = buildUrl("$gifBaseUrl?page=$pageIndex&maxResult=$maxResult")
            LogHelper.e(TAG + "getGIFData:$url")
            val readResult = URL(url).readText()
            gifList = JsonHelper.gsonToBean(readResult, GifImage::class.java).showapi_res_body.contentlist
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return gifList!!
    }

    fun getBeautyGirlData(pageIndex: Int): List<BeautyGirl> {
        val jokeList: List<BeautyGirl>? = null
        return jokeList!!
    }

    fun getLikeArticeData(pageIndex: Int): List<BeautyGirl> {
        val jokeList: List<BeautyGirl>? = null
        return jokeList!!
    }
}
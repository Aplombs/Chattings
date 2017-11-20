package com.chat.im.test.jsonbean

/**
 * GIF图JavaBean
 */
data class GifImage(val showapi_res_code: String,
                    val showapi_res_error: String,
                    val showapi_res_body: GifBody)


data class GifBody(val allNum: String,
                   val allPages: String,
                   val currentPage: String,
                   val maxResult: String,
                   val contentlist: List<Gif>)


data class Gif(val title: String,
               val img: String)
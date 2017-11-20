package com.chat.im.test.jsonbean

/**
段子JavaBean
 */
data class JokeText(val showapi_res_code: String,
                    val showapi_res_error: String,
                    val showapi_res_body: JokeBody)

data class JokeBody(val allNum: String,
                    val allPages: String,
                    val contentlist: List<JokeContent>,
                    val currentPage: String,
                    val maxResult: String,
                    val ret_code: String)

data class JokeContent(val ct: String,
                       val text: String,
                       val title: String,
                       val type: String)
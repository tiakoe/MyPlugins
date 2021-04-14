package com.a.kotlin_library.test2.okhttp

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


fun main() {
    val client = OkHttpClient() //创建OkHttpClient对象

    val request: Request = Request.Builder()
            .url("https://www.wanandroid.com/article/list/0/json") //请求链接
            .build() //创建Request对象

    val response: Response = client.newCall(request).execute() //获取Response对象
    //读取返回内容
    println(response.body())
}



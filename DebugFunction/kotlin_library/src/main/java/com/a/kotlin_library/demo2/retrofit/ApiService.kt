package com.a.kotlin_library.demo2.retrofit

import com.a.kotlin_library.demo2.bean.Response
import com.a.kotlin_library.demo2.bean.User
import com.a.kotlin_library.demo2.bean.home.HomeData
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface ApiService {
    companion object {
        val homeBase: String = "https://www.wanandroid.com/"

        fun create(): ApiService {
            val builder = OkHttpClient.Builder()
            val TIME_OUT: Long = 10
            try {
                builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val okHttpClient = builder.build()
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(homeBase)
                    .client(okHttpClient)
                    .build()

            return retrofit.create(ApiService::class.java)
        }
    }


    //    https://www.wanandroid.com/article/list/0/json
    @GET("/article/list/{page}/json")
    fun getAllDatas(@Path("page") page: Int): Observable<HomeData>


    @GET("/article/list/{page}/json")
    suspend fun getHomeArticles(@Path("page") page: Int): HomeData


    @FormUrlEncoded
    @POST("user/login")
    suspend fun loginAsync(@Field("username") username: String,
                           @Field("password") pwd: String): Response<User>

    @FormUrlEncoded
    @POST("user/register")
    suspend fun registerAsync(
            @Field("username") username: String, @Field("password") pwd: String, @Field("repassword") rpwd: String
    ): Response<Any>

}

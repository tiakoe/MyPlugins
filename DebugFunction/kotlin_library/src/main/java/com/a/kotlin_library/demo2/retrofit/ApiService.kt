package com.a.kotlin_library.demo2.retrofit

import com.a.kotlin_library.demo2.bean.HomeData
import com.a.kotlin_library.demo2.bean.Response
import com.a.kotlin_library.demo2.bean.User
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiService {
    companion object {
        val homeBase: String = "https://www.wanandroid.com/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(homeBase)
                    .build()

            return retrofit.create(ApiService::class.java)
        }
    }


    //    https://www.wanandroid.com/article/list/0/json
    @GET("/article/list/{page}/json")
    fun getAllDatas(@Path("page") page: String): Observable<HomeData>


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

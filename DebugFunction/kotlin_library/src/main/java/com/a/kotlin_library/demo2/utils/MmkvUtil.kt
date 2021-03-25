package com.a.kotlin_library.demo2.utils

import android.text.TextUtils
import com.a.kotlin_library.demo2.bean.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV

object MmkvUtil {
    //   获取保存的账户信息
    fun getUser(): User? {
        val kv = MMKV.mmkvWithID("app")
        val userStr = kv.decodeString("user")
        return if (TextUtils.isEmpty(userStr)) {
            null
        } else {
            Gson().fromJson(userStr, User::class.java)
        }
    }

    //    设置账户信息
    fun saveUser(userResponse: User?) {
        val kv = MMKV.mmkvWithID("app")
        if (userResponse == null) {
            kv.encode("user", "")
            setIsLogin(false)
        } else {
            kv.encode("user", Gson().toJson(userResponse))
            setIsLogin(true)
        }

    }

    //    是否已经登录
    fun isLogin(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("login", false)
    }

    fun setIsLogin(isLogin: Boolean) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode("login", isLogin)
    }

    fun isFirst(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("first", true)
    }

    fun setFirst(first: Boolean): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.encode("first", first)
    }

    //    首页是否开启获取指定文章
    fun isNeedTop(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("top", true)
    }

    //    设置首页是否开启获取指定文章
    fun setIsNeedTop(isNeedTop: Boolean): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.encode("top", isNeedTop)
    }

    /**
     * 获取搜索历史缓存数据
     */
    fun getSearchHistoryData(): ArrayList<String> {
        val kv = MMKV.mmkvWithID("cache")
        val searchCacheStr = kv.decodeString("history")
        if (!TextUtils.isEmpty(searchCacheStr)) {
            return Gson().fromJson(searchCacheStr, object : TypeToken<ArrayList<String>>() {}.type)
        }
        return arrayListOf()
    }

    fun setSearchHistoryData(searchResponseStr: String) {
        val kv = MMKV.mmkvWithID("cache")
        kv.encode("history", searchResponseStr)
    }
}

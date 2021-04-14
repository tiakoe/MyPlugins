package com.a.kotlin_library.demo2.fragment.home

import android.content.Context
import android.content.SharedPreferences

internal object ShareUtil {
    @Volatile
    private var mSharedPreferences: SharedPreferences? = null

    @Synchronized
    fun initSharedPreferences(context: Context): SharedPreferences? {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("refresh_time", Context.MODE_PRIVATE)
        }
        return mSharedPreferences
    }

    fun writeRefreshTime(key: String?, value: Long) {
        val editor = mSharedPreferences!!.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getRefreshTime(key: String?): Long {
        return mSharedPreferences!!.getLong(key, 0)
    }
}

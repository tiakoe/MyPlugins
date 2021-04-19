package com.a.kotlin_library.demo2.utils

import android.util.Log

fun <T> kLog(vararg arr: T) {
    val it = Thread.currentThread().stackTrace
    Log.d("KLog", "${it[3].methodName}(${it[3].fileName}:${it[3].lineNumber})--->>${arr.asList()}")
}

fun <T> kLogE(vararg arr: T) {
    val it = Thread.currentThread().stackTrace
    Log.e("KLog", "${it[3].methodName}(${it[3].fileName}:${it[3].lineNumber})--->>${arr.asList()}")
}

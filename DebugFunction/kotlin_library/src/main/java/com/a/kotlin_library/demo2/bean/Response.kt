package com.a.kotlin_library.demo2.bean

data class Response<T>(val errorCode: Int, val errorMsg: String, val data: T) {
}

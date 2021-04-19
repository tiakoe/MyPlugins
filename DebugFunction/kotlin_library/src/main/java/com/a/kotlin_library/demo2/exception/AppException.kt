package com.a.kotlin_library.demo2.exception

class AppException(//错误码
        var errCode: Int, error: String?, errorLog: String? = "") : Exception(error) {

    private var errorMsg: String = error ?: "请求失败，请稍后再试"
    var errorLog: String? = errorLog ?: this.errorMsg
}

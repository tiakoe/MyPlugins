package com.a.kotlin_library.demo2.bean.home

import com.a.kotlin_library.room.table.HomeTable

data class HomeData(
        val `data`: Data,
        val errorCode: Int,
        val errorMsg: String
) {
    override fun toString(): String {
        return "HomeData(`data`=$`data`, errorCode=$errorCode, errorMsg='$errorMsg')"
    }
}

data class Data(
        val curPage: Int,
        val datas: List<HomeTable>,
        val offset: Int,
        val over: Boolean,
        val pageCount: Int,
        val size: Int,
        val total: Int
)


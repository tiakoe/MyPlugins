package com.a.kotlin_library.demo2.bean.home

import com.a.kotlin_library.room.table.BannerTable

data class BannerData(
        val `data`: List<BannerTable>,
        val errorCode: Int,
        val errorMsg: String
)



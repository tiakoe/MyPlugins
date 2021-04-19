package com.a.kotlin_library.demo2.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a.kotlin_library.demo2.utils.kLog
import com.a.kotlin_library.room.table.HomeTable

class HomeViewModel : ViewModel() {
    var homeArticlesData: MutableLiveData<List<HomeTable>> = MutableLiveData()
    var count: MutableLiveData<Int> = MutableLiveData()

    fun clickSearch() {
        kLog(402173131, "click")
    }
}

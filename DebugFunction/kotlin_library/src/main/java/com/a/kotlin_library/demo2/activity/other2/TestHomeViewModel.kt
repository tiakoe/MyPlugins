package com.a.kotlin_library.demo2.activity.other2

import androidx.lifecycle.MutableLiveData
import com.a.kotlin_library.demo2.view_model.base.BaseViewModel
import com.a.kotlin_library.room.table.HomeTable

class TestHomeViewModel : BaseViewModel() {
    var homeArticlesData: MutableLiveData<List<HomeTable>> = MutableLiveData()
}

package com.a.kotlin_library.demo2.fragment.search

import androidx.lifecycle.MutableLiveData
import com.a.kotlin_library.demo2.bean.ArticleResponse
import com.a.kotlin_library.demo2.bean.PagerResponse
import com.a.kotlin_library.demo2.bean.home.SearchResponse
import com.a.kotlin_library.demo2.view_model.base.BaseViewModel

class SearchViewModel : BaseViewModel() {
    var hotData: MutableLiveData<ResultState<ArrayList<SearchResponse>>> = MutableLiveData()

    var searchResultData: MutableLiveData<ResultState<ArrayList<PagerResponse<ArticleResponse>>>> = MutableLiveData()

    var historyData: MutableLiveData<ArrayList<String>> = MutableLiveData()

    /**
     * 根据字符串搜索结果
     */
//    fun getSearchResultData(searchKey: String, isRefresh: Boolean) {
//        if (isRefresh) {
//            pageNo = 0
//        }
//        request(
//                { apiService.getSearchDataByKey(pageNo, searchKey) },
//                seachResultData
//        )
//    }

}

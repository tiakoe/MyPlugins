package com.a.kotlin_library.demo2.fragment.home

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.a.kotlin_library.R
import com.a.kotlin_library.databinding.FragmentHomeBinding
import com.a.kotlin_library.demo2.bean.home.HomeData
import com.a.kotlin_library.demo2.fragment.base.BaseFragment
import com.a.kotlin_library.demo2.layout.LoadMoreView
import com.a.kotlin_library.demo2.layout.RefreshLinerLayout
import com.a.kotlin_library.demo2.layout.RefreshView
import com.a.kotlin_library.demo2.retrofit.ApiService
import com.a.kotlin_library.room.WanDatabase
import com.a.kotlin_library.room.repository.HomeRepository
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    private var mHomeFragmentAdapter: HomeFragmentAdapter? = null
    private var mPage: Int = 0
    private var limit: Int = 15  //需要小于每页item数量
    private var isFirst = true
    private var curNum = 0

    companion object {
        private const val RV_REFRESH_TIME = "RV_Refresh_Time"
    }

    private lateinit var repository: HomeRepository
    private lateinit var database: WanDatabase


    override fun layoutId(): Int = R.layout.fragment_home
    override fun initView(savedInstanceState: Bundle?) {
        ShareUtil.initSharedPreferences(context)

        database = WanDatabase.getDatabase(context)
        val homeDao = database.homeDao()
        repository = HomeRepository(homeDao)
        repository.deleteAll()

        refresh_recycler_view.layoutManager = LinearLayoutManager(context)
        refresh_recycler_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        mHomeFragmentAdapter = HomeFragmentAdapter(context)
        mHomeFragmentAdapter!!.setHasStableIds(true)
        refresh_recycler_view.itemAnimator = null;
        refresh_recycler_view.adapter = mHomeFragmentAdapter
        configRefreshLinerLayout()
        observe()

        if (isFirst) {
            requestHomeData()
        } else {
            queryHomeData()
        }
    }


    private fun configRefreshLinerLayout() {
        val refreshView = RefreshView(context)
        val refreshTime = ShareUtil.getRefreshTime(RV_REFRESH_TIME)
        if (refreshTime > 0) {
            refreshView.setRefreshTime(Date(refreshTime))
        }
        refresh_layout.setHeaderView(refreshView)
        refresh_layout.setFooterView(LoadMoreView(context))

        refresh_layout.setOnRefreshListener(object : RefreshLinerLayout.OnRefreshListener {
            override fun onRefresh() {
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    repository.getDataLimit(limit)
                    withContext(Dispatchers.Main) {
                        viewModel.homeArticlesData.value = repository.allHomeData
                        refresh_layout.post {
                            ShareUtil.writeRefreshTime(RV_REFRESH_TIME, System.currentTimeMillis())
                            refresh_layout.finishRefresh(true)
                        }
                    }
                }
            }
        })

        refresh_layout.setOnLoadMoreListener(object : RefreshLinerLayout.OnLoadMoreListener {
            override fun onLoadMore() {
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    val total = repository.getTotalNum()
                    curNum = limit
                    limit += 15
                    if (limit > total) {
                        var result: HomeData? = null
                        runCatching {
                            result = ApiService.create().getHomeArticles(mPage++)
                        }.onFailure {
                            it.printStackTrace()
                            refresh_layout.post {
                                refresh_layout.finishLoadMore(true, false)
                            }
                            return@launch
                        }
//                        database.withTransaction {
                        result?.data?.let { repository.insertAll(it.datas) }
                        repository.getDataLimit(limit)
//                        }
                        withContext(Dispatchers.Main) {
                            if (total == repository.getTotalNum()) {
                                refresh_layout.post {
                                    refresh_layout.finishLoadMore(true, false)
                                }
                            } else {
                                viewModel.homeArticlesData.value = repository.allHomeData
                                refresh_layout.post {
                                    refresh_layout.finishLoadMore(true, true)
                                }
                            }
                        }
                    } else {
                        repository.getDataLimit(limit)
                        withContext(Dispatchers.Main) {
                            viewModel.homeArticlesData.value = repository.allHomeData
                            refresh_layout.post {
                                refresh_layout.finishLoadMore(true, true)
                            }
                        }
                    }
                }
                mHomeFragmentAdapter?.let {
                }
            }
        })
    }

    fun observe() {
        viewModel.homeArticlesData.observe(this, {
            it?.let {
                val homeFragmentAdapter = refresh_recycler_view.adapter as HomeFragmentAdapter
//            和旧值进行比较，若相等则保留旧值，不等替换新值，最后删除旧值
                homeFragmentAdapter.updateDataAsync(it)
            }
        })

//        recyclerView.requestChildRectangleOnScreen()

    }

    private fun requestHomeData() {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val result: HomeData = ApiService.create().getHomeArticles(mPage++)
                repository.insertAll(result.data.datas)
                repository.getDataLimit(limit)
                withContext(Dispatchers.Main) {
                    viewModel.homeArticlesData.value = repository.allHomeData
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    private fun queryHomeData() {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            repository.getDataLimit(limit)
            withContext(Dispatchers.Main) {
                viewModel.homeArticlesData.value = repository.allHomeData
            }
        }
    }


}

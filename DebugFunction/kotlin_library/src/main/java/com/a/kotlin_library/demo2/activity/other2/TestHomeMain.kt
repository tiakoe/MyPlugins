package com.a.kotlin_library.demo2.activity.other2

import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.a.kotlin_library.R
import com.a.kotlin_library.databinding.TestActivityHomeBinding
import com.a.kotlin_library.demo2.HomeAdapter
import com.a.kotlin_library.demo2.activity.base.BaseActivity
import com.a.kotlin_library.demo2.activity.other2.listener.OptionOnScrollListener
import com.a.kotlin_library.demo2.activity.other2.listener.RecyclerViewOnScrollListener
import com.a.kotlin_library.demo2.activity.other2.test5.CustomRefreshLinerLayout.*
import com.a.kotlin_library.demo2.bean.home.HomeData
import com.a.kotlin_library.demo2.retrofit.ApiService
import com.a.kotlin_library.room.WanDatabase
import com.a.kotlin_library.room.repository.HomeRepository
import com.a.kotlin_library.room.table.HomeTable
import kotlinx.android.synthetic.main.test_activity_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestHomeMain : BaseActivity<TestActivityHomeBinding, TestHomeViewModel>() {
    private lateinit var repository: HomeRepository
    private lateinit var database: WanDatabase

    private lateinit var scrollListener: RecyclerViewOnScrollListener
    private lateinit var mRefreshStateListener: OnRefreshStateListener
    private lateinit var mLoadStateListener: OnLoadStateListener
    private lateinit var mRequestListener: OnRequestDataStateListener

    private var clickCount = 0

    override fun layoutId(): Int {
        return R.layout.test_activity_home
    }

    override fun beforeInit(savedInstanceState: Bundle?) {
    }

    var newlist = arrayListOf<HomeTable>()
    override fun observe() {
        viewModel.homeArticlesData.observe(this, {
            it?.let {
                val homeAdapter = test_recyclerview.adapter as HomeAdapter
//            和旧值进行比较，若相等则保留旧值，不等替换新值，最后删除旧值
                homeAdapter.updateDataAsync(it)
            }
        })

        addItem.setOnClickListener {
            clickCount += 1
            val adapter: HomeAdapter = test_recyclerview.adapter as HomeAdapter
            viewModel.homeArticlesData.value?.get(0)?.let { it1 ->
                val item = it1.copy()
                newlist.add(
                        item.apply {
                            if (clickCount % 3 == 0) {
                                this.id = clickCount
                                this.title = "clickCount_$clickCount"
                                this.desc = "clickCount_$clickCount"
                            }
                        })
            }
            adapter.updateDataAsync(newlist)
        }
    }

    private fun initScrollListener() {
        scrollListener = RecyclerViewOnScrollListener(object : OptionOnScrollListener {
            override fun onLoadMore() {
            }

            override fun onRefresh() {
            }
        })

        mRefreshStateListener = object : OnRefreshStateListener {

            override fun onDragChange(view: View, state: Int) {
            }

            override fun onDragAnim(view: View, state: Int) {
            }

            override fun onDragAnimStart(view: View, state: Int) {
            }

            override fun onAnimDisplay(view: View) {
            }

            override fun onAnimComplete(view: View, state: Int) {
            }
        }
        mLoadStateListener = object : OnLoadStateListener {

            override fun onDragChange(view: View, state: Int) {
            }

            override fun onDragAnim(view: View, state: Int) {
            }

            override fun onDragAnimStart(view: View, state: Int) {
            }

            override fun onAnimDisplay(view: View) {
            }

            override fun onAnimComplete(view: View, state: Int) {
            }
        }

        mRequestListener = object : OnRequestDataStateListener {
            override fun request(): Boolean {
                return true
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        initScrollListener()

        database = WanDatabase.getDatabase(this)
        val homeDao = database.homeDao()
        repository = HomeRepository(homeDao)

        test_recyclerview.layoutManager = LinearLayoutManager(this)
        test_recyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        test_recyclerview.adapter = HomeAdapter(this)
        test_recyclerview.addOnScrollListener(scrollListener)

        customRefreshLinerLayout.setRefreshListener(mRefreshStateListener)
        customRefreshLinerLayout.setLoadListener(mLoadStateListener)
        customRefreshLinerLayout.setRequestListener(mRequestListener)


        requestAndSave()
        Thread.sleep(3000)
        queryHomeData()
    }

    private fun queryHomeData() {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            repository.getData()
            withContext(Dispatchers.Main) {
                viewModel.homeArticlesData.value = repository.allHomeData
            }
        }
    }

    private fun requestAndSave() {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val result: HomeData = ApiService.create().getHomeArticles(0)
                repository.insertAll(result.data.datas)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    override fun bindViewModel() {
    }


}

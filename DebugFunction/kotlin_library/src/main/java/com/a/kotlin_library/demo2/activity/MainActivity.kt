package com.a.kotlin_library.demo2.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.a.kotlin_library.R
import com.a.kotlin_library.demo2.HomeAdapter
import com.a.kotlin_library.demo2.application.MyApplication
import com.a.kotlin_library.demo2.bean.DataX
import com.a.kotlin_library.demo2.retrofit.ApiService
import com.a.kotlin_library.demo2.room.HomeTable
import com.a.kotlin_library.demo2.view_model.HomeViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main_home_page.*


class MainActivity : AppCompatActivity() {

    private var disposable: Disposable? = null
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home_page)
        showData()
        homeViewModel = MyApplication.getApplication()?.let { HomeViewModel(it) }!!
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun showData() {
        disposable =
                ApiService.create().getAllDatas("0")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { result -> onSuccess(result.data.datas.toList()) },
                                { error -> Log.e("ERROR", error.message + " ") })
    }

    private fun onSuccess(dataList: List<DataX>) {
        recyclerview.adapter = HomeAdapter(this, dataList)
        val temp = ArrayList<HomeTable>()
        dataList.forEach { e ->
            with(e) {
                temp.add(HomeTable(apkLink, audit, author, canEdit, chapterId, chapterName, collect, courseId, desc, descMd, envelopePic, fresh, host, id, link, niceDate, niceShareDate, origin, prefix, projectLink, publishTime, realSuperChapterId, selfVisible, shareDate, shareUser, superChapterId, superChapterName, tags, title, type, userId, visible, zan))
            }
        }
        homeViewModel.insertAll(temp)
        println("ddd")
    }

}

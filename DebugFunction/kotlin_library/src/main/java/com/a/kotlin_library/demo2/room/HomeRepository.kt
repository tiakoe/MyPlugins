package com.a.kotlin_library.demo2.room

import androidx.lifecycle.LiveData
import java.util.concurrent.Executors

class HomeRepository(private val homeDao: HomeDao) {

    val allHomeData: LiveData<List<HomeTable>> = homeDao.getHomeData()

    fun insert(homeTable: HomeTable) {
        try {
            homeDao.insert(homeTable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun insertAll(datas: List<HomeTable>) {
        try {
            Executors.newSingleThreadExecutor().execute { homeDao.insertAll(datas) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun update(homeTable: HomeTable) {
        homeDao.update(homeTable)
    }

    fun delete() {
    }

    fun deleteAll() {
        homeDao.deleteAll()
    }

}

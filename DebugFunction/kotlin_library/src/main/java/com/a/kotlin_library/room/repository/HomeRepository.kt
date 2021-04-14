package com.a.kotlin_library.room.repository

import com.a.kotlin_library.room.dao.HomeDao
import com.a.kotlin_library.room.table.HomeTable

class HomeRepository(private val homeDao: HomeDao) {

    var allHomeData: List<HomeTable> = listOf()

    suspend fun getData() {
        allHomeData = homeDao.getHomeData()
    }

    suspend fun getDataLimit(limit: Int) {
        allHomeData = homeDao.getHomeDataLimit(limit)
    }

    suspend fun getTotalNum(): Int {
        return homeDao.getTotalNum()
    }

    suspend fun insert(homeTable: HomeTable) {
        try {
            homeDao.insert(homeTable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun insertAll(datas: List<HomeTable>) {
        homeDao.insertAll(datas)
    }

    suspend fun update(homeTable: HomeTable) {
        homeDao.update(homeTable)
    }

    suspend fun delete() {
    }

    fun deleteAll() {
        homeDao.deleteAll()
    }

}

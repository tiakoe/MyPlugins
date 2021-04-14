package com.a.kotlin_library.room.repository

import com.a.kotlin_library.room.dao.BannerDao
import com.a.kotlin_library.room.table.BannerTable
import java.util.concurrent.Executors

class BannerRepository(private val bannerDao: BannerDao) {

    var allHomeData: List<BannerTable>? = listOf()

    fun getData() {
        allHomeData = bannerDao.getHomeData()
    }

    fun insert(bannerTable: BannerTable) {
        try {
            bannerDao.insert(bannerTable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun insertAll(datas: List<BannerTable>) {
        try {
            Executors.newSingleThreadExecutor().execute { bannerDao.insertAll(datas) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun update(bannerTable: BannerTable) {
        bannerDao.update(bannerTable)
    }

    fun delete() {
    }

    fun deleteAll() {
        bannerDao.deleteAll()
    }

}

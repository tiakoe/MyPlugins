package com.a.kotlin_library.room.dao

import androidx.room.*
import com.a.kotlin_library.room.table.BannerTable


@Dao
interface BannerDao {

    @Query("SELECT * from bannerTable")
    fun getHomeData(): List<BannerTable>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(bannerTable: BannerTable)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(datas: List<BannerTable>)

    @Update
    fun update(bannerTable: BannerTable)

    @Query("DELETE From bannerTable where title = :title")
    fun deleteTable(title: String)

    @Query("DELETE FROM bannerTable")
    fun deleteAll()
}

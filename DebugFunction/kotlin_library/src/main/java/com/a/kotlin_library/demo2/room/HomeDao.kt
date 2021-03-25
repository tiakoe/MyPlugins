package com.a.kotlin_library.demo2.room

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface HomeDao {

    @Query("SELECT * from homeData")
    fun getHomeData(): LiveData<List<HomeTable>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(homeTable: HomeTable)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(datas: List<HomeTable>)

    @Update
    fun update(homeTable: HomeTable)

    @Query("DELETE From homeData where title = :title")
    fun deleteTable(title: String)

    @Query("DELETE FROM homeData")
    fun deleteAll()
}

package com.a.kotlin_library.room.dao

import androidx.room.*
import com.a.kotlin_library.room.table.HomeTable


@Dao
interface HomeDao {

    @Query("SELECT * from homeTable")
    suspend fun getHomeData(): List<HomeTable>

    @Query("SELECT * from homeTable limit :count")
    suspend fun getHomeDataLimit(count: Int): List<HomeTable>


    @Query("SELECT COUNT(1) ROWS FROM homeTable")
    suspend fun getTotalNum(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(homeTable: HomeTable)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(datas: List<HomeTable>)

    @Update
    suspend fun update(homeTable: HomeTable)

    @Query("DELETE From homeTable where title = :title")
    suspend fun deleteTable(title: String)

    @Query("DELETE FROM homeTable")
    fun deleteAll()
}

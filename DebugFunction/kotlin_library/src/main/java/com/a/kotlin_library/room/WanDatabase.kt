package com.a.kotlin_library.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.a.kotlin_library.room.dao.HomeDao
import com.a.kotlin_library.room.table.BannerTable
import com.a.kotlin_library.room.table.HomeTable

@Database(entities = [HomeTable::class, BannerTable::class], version = 2, exportSchema = false)
abstract class WanDatabase : RoomDatabase() {

    abstract fun homeDao(): HomeDao

    companion object {
        @Volatile
        private var INSTANCE: WanDatabase? = null

        fun getDatabase(context: Context): WanDatabase {
            return INSTANCE
                    ?: synchronized(this) {
                        val instance = Room.databaseBuilder(
                                context.applicationContext,
                                WanDatabase::class.java,
                                "WanAndroidDatabase"
                        )
                                .fallbackToDestructiveMigration()
                                .allowMainThreadQueries()
                                .build()
                        INSTANCE = instance
                        instance
                    }
        }
    }

}

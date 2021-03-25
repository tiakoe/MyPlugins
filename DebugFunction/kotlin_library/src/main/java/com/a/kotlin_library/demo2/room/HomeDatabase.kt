package com.a.kotlin_library.demo2.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HomeTable::class], version = 1, exportSchema = false)
abstract class HomeDatabase : RoomDatabase() {

    abstract fun homeDao(): HomeDao

    companion object {
        @Volatile
        private var INSTANCE: HomeDatabase? = null

        fun getDatabase(context: Context): HomeDatabase {
            return INSTANCE
                    ?: synchronized(this) {
                        val instance = Room.databaseBuilder(
                                context.applicationContext,
                                HomeDatabase::class.java,
                                "homeData"
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

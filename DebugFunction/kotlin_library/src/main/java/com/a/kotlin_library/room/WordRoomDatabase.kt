//package com.a.kotlin_library.room
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import androidx.sqlite.db.SupportSQLiteDatabase
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//
//@Database(entities = [Word::class], version = 1)
//abstract class WordRoomDatabase : RoomDatabase() {
//
//    abstract fun wordDao(): WordDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: WordRoomDatabase? = null
//
//        fun getDatabase(context: Context, scope: CoroutineScope): WordRoomDatabase {
//            return INSTANCE ?: synchronized(this) {
//                var instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        WordRoomDatabase::class.java,
//                        "word_database"
//                )
//                        // when migrate database, wipes and rebuilds database
//                        .fallbackToDestructiveMigration()
//                        .addCallback(WordDatabaseCallback(scope))
//                        .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//
//        class WordDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//                // init database content
//                INSTANCE?.let { database ->
//                    scope.launch(Dispatchers.IO) {
//                        populateDatabase(database.wordDao())
//                    }
//                }
//            }
//        }
//
//        suspend fun populateDatabase(wordDao: WordDao) {
//            wordDao.deleteAll()
//            var word = Word("Hello")
//            wordDao.insert(word)
//            word = Word("World!")
//            wordDao.insert(word)
//        }
//    }
//}

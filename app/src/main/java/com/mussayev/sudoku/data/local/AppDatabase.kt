package com.mussayev.sudoku.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mussayev.sudoku.data.model.SavedGame

//@Database(entities = [
//    SavedGame::class
//], version = 3)
//abstract class AppDatabase : RoomDatabase() {
////    abstract fun getSudokuDao(): SavedGameDao
//
////    companion object {
////        @Volatile
////        private var INSTANCE: AppDatabase? = null
////
////        fun getDatabase(context: Context): AppDatabase {
////            return INSTANCE ?: synchronized(this) {
////                val instance = Room.databaseBuilder(
////                    context.applicationContext,
////                    AppDatabase::class.java,
////                    "app_database"
////                ).build()
////                INSTANCE = instance
////                instance
////            }
////        }
////    }
//}

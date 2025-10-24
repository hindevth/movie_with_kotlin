package com.hin.movie.data.local.databse

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hin.movie.data.entities.EpisodeHistory
import com.hin.movie.data.entities.MovieHistory
import com.hin.movie.data.local.dao.MovieHistoryDao

@Database(entities = [MovieHistory::class, EpisodeHistory::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieHistoryDao(): MovieHistoryDao
}
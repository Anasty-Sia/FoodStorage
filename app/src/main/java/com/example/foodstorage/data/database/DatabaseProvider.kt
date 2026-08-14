package com.example.foodstorage.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    fun createDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "food_storage.db"
        ).build()
    }
}
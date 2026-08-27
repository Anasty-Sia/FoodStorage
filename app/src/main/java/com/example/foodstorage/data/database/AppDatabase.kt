package com.example.foodstorage.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodstorage.data.converter.ProductConverters

@Database(entities = [ProductEntity::class], version = 1, exportSchema = true)
@TypeConverters(ProductConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
}
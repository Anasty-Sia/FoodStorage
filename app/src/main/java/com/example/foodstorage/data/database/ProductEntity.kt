package com.example.foodstorage.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "products")
data class ProductEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "quantity") val quantity: Double,
    @ColumnInfo(name = "storagePlace") val storagePlace: String,
    @ColumnInfo(name = "shelfLife") val shelfLife: LocalDate,
)
package com.example.foodstorage.data.converter

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProductConverters {

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    @TypeConverter()
    fun localDateToString(shelfLife: LocalDate): String {
        return shelfLife.format(formatter)
    }

    @TypeConverter
    fun stringToLocalDate(shelfLife: String): LocalDate {
        return LocalDate.parse(shelfLife, formatter)
    }
}


package com.tfg.bargaingames.model.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.tfg.bargaingames.model.Price

class GameConverters {
    @TypeConverter
    fun fromJsonStr(value: String?): Price? {
        return value?.let { Gson().fromJson(it, Price::class.java) }
    }

    @TypeConverter
    fun fromRating(value: Price?): String? {
        return value?.let { Gson().toJson(it) }
    }
}
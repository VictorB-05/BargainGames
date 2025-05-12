package com.tfg.bargaingames.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tfg.bargaingames.model.detail.GameData

@Database(entities = [GameData::class], version = 2)
@TypeConverters(GameConverters::class)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}
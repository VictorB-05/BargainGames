package com.tfg.bargaingames.model.database

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class GameApplication : Application() {
    companion object {
        lateinit var database: GameDatabase
    }

    override fun onCreate() {
        super.onCreate()

        val MIGRATION_1_2 = object : Migration(1,2){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE Game ADD COLUMN favorito INTEGER DEFAULT 0 NOT NULL")
                db.execSQL("ALTER TABLE Game ADD COLUMN deseado INTEGER DEFAULT 0 NOT NULL")
            }
        }

        val MIGRATION_2_3 = object : Migration(2,3){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE Game ADD COLUMN free INTEGER DEFAULT 0 NOT NULL")
            }
        }

        database = Room.databaseBuilder(this,
            GameDatabase::class.java,
            "GameDatabase")
            .addMigrations(MIGRATION_1_2)
            .addMigrations(MIGRATION_2_3)
            .build()
    }
}
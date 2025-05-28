package com.tfg.bargaingames.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.tfg.bargaingames.model.detail.GameData

@Dao
interface GameDao {
    @Query("SELECT * FROM Game")
    fun getAllGame(): MutableList<GameData>

    @Insert
    fun addGame(game: GameData): Long

    @Delete
    fun deleteGame(game: GameData)

    @Query("SELECT * FROM Game Where favorito = 1")
    fun getFavoriteGame(): MutableList<GameData>

    @Query("SELECT * FROM Game Where deseado = 1")
    fun getWishGame(): MutableList<GameData>

    @Query("SELECT * FROM Game Where id= :id")
    suspend fun getGame(id: Int): GameData?

    @Update
    fun update(game: GameData)

}
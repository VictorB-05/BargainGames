package com.tfg.bargaingames.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tfg.bargaingames.model.detail.GameData

@Dao
interface GameDao {
    @Query("SELECT * FROM Game")
    fun getAllGame(): MutableList<GameData>

    @Query("SELECT * FROM Game Where favorito = 1")
    fun getFavoriteGame(): MutableList<GameData>

    @Query("SELECT * FROM Game Where deseado = 1")
    fun getWishGame(): MutableList<GameData>

    @Insert
    fun addGame(game: GameData): Long

    @Query("SELECT * FROM Game Where id= :id")
    suspend fun getGame(id: Int): GameData?

    @Query("UPDATE Game SET favorito = :isFavorito WHERE id = :id")
    fun updateFavorito(id: Int, isFavorito: Boolean)

    @Query("UPDATE Game SET deseado = :isDeseado WHERE id = :id")
    fun updateDeseado(id: Int, isDeseado: Boolean)

}
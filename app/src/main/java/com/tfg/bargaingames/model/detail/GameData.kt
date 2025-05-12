package com.tfg.bargaingames.model.detail

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.tfg.bargaingames.model.GameItem
import com.tfg.bargaingames.model.Price

@Entity(tableName = "Game")
data class GameData (
    @SerializedName("steam_appid")
    @PrimaryKey override val id: Int,
    override var name : String,
    @SerializedName("detailed_description")
    var description : String,
    @SerializedName("header_image")
    var image : String,
    @SerializedName("capsule_imagev5")
    var capsuleImage : String,
    @SerializedName("price_overview")
    var price: Price?,

    // Campos locales, no vienen del JSON
    var favorito: Boolean = false,
    var deseado: Boolean = false
) : GameItem

package com.tfg.bargaingames.model.detail

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Ignore
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

    // Campos locales
    var favorito: Boolean = false,
    var deseado: Boolean = false,

    @SerializedName("is_free")
    var free: Boolean
) : GameItem
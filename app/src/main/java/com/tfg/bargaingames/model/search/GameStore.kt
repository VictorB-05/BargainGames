package com.tfg.bargaingames.model.search

import com.google.gson.annotations.SerializedName
import com.tfg.bargaingames.model.GameItem
import com.tfg.bargaingames.model.Price

data class GameStore(
    override val id: Int,
    override val name: String,
    val type: String,
    val price: Price?,
    @SerializedName("tiny_image")
    val tinyImage: String,
) : GameItem

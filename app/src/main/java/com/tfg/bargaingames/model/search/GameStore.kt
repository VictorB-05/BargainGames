package com.tfg.bargaingames.model.search

import com.google.gson.annotations.SerializedName

data class GameStore(
    val id: Int,
    //val type: String,
    val name: String,
    val price: Price?,
    @SerializedName("tiny_image")
    val tinyImage: String,
    @SerializedName("controller_support")
    val controllerSupport: String?
)

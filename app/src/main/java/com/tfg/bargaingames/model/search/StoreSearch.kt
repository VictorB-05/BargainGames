package com.tfg.bargaingames.model.search

import com.google.gson.annotations.SerializedName

data class StoreSearch (
    @SerializedName("total")
    val number: Int,

    @SerializedName("items")
    val games: List<GameStore>
)

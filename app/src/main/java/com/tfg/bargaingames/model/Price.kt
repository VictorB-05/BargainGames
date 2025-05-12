package com.tfg.bargaingames.model

import com.google.gson.annotations.SerializedName

data class Price(
    val currency: String,
    val initial: Int,
    val final: Int,
    @SerializedName("discount_percent")
    val discountPercent: Int
)
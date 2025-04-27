package com.tfg.bargaingames.model.game

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class Game(
    val id: Int,
    val type: String,
    val name: String,
    val discounted: Boolean,

    @SerializedName("discount_percent")
    val discountedPercent: Int,

    @SerializedName("original_price")
    val originalPrice: Int,

    @SerializedName("final_price")
    val finalPrice: Int,

    val currency: String,

    @SerializedName("large_capsule_image")
    val largeImage: String,

    @SerializedName("small_capsule_image")
    val smallImage: String,

    @SerializedName("discount_expiration")
    val discountExpirationNumber: Long?,

    val discountExpiration: LocalDate? = discountExpirationNumber?.let { Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault()).toLocalDate() }

    )
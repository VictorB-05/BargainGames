package com.tfg.bargaingames

import java.time.LocalDate

data class Game(
    val id: Int,
    val type: String,
    val name: String,
    val discounted: Boolean,
    val discountedPercent: Int,
    val originalPrice: Int,
    val finalPrice: Int,
    val currency: String,
    val largeImage: String,
    val smallImage: String,
    val discountExpiration: LocalDate
)
package com.tfg.bargaingames.model.game

import com.google.gson.annotations.SerializedName

data class FeaturedCategories(
    @SerializedName("specials")
    val specials: GameCategory?,

    @SerializedName("coming_soon")
    val comingSoon: GameCategory?,

    @SerializedName("top_sellers")
    val topSellers: GameCategory?,

    @SerializedName("new_releases")
    val newReleases: GameCategory?,

    //val genres: List<GenreCategory>?,

    val status: Int
)

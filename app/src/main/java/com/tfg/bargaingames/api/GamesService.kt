package com.tfg.bargaingames.api

import com.tfg.bargaingames.api.Constantes.FETURED
import com.tfg.bargaingames.api.Constantes.SEARCH
import com.tfg.bargaingames.model.game.FeaturedCategories
import com.tfg.bargaingames.model.search.StoreSearch
import retrofit2.http.GET
import retrofit2.http.Query

interface GamesService {
    @GET(FETURED)
    suspend fun getFeaturedCategories(): FeaturedCategories

    @GET(SEARCH)
    suspend fun getStoreSearch(
        @Query("term") term: String,
        @Query("l") language: String = "spanish",
        @Query("cc") countryCode: String = "ES"
    ): StoreSearch

}

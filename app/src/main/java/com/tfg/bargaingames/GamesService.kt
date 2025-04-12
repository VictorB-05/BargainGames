package com.tfg.bargaingames

import retrofit2.http.GET

interface GamesService {
    @GET(Constantes.BASE_URL)
    suspend fun getFeaturedGames() : List<Game>
}
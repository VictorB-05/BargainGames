package com.tfg.bargaingames.model.game

data class GameCategory (
    val id: String,
    val name: String,
    val items: List<GameCategorized>
)

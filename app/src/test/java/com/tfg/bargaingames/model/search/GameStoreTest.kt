package com.tfg.bargaingames.model.search

import com.tfg.bargaingames.model.game.GameCategorized
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class GameStoreTest{
    @Test
    fun testZeroPriceIsHandled() {
        val game = GameCategorized(
            id = 2,
            name = "Free Game",
            discounted = true,
            discountedPercent = 120,
            originalPrice = 1000,
            finalPrice = 10,
            currency = "EUR",
            smallImage = "url",
            discountExpirationNumber = null
        )

        assertEquals(0, game.finalPrice)
        assertEquals(100, game.discountedPercent)
        assertTrue(game.discounted)
    }

    @Test
    fun testInvalidDiscountPercent() {
        val game = GameCategorized(
            id = 3,
            name = "Invalid Discount",
            discounted = true,
            discountedPercent = 150, // fuera del 0–100
            originalPrice = 1000,
            finalPrice = 200,
            currency = "EUR",
            smallImage = "url",
            discountExpirationNumber = null
        )

        assertEquals(0, game.finalPrice)
        assertEquals(100, game.discountedPercent)
    }

    @Test
    fun testFalseDiscount() {
        val game = GameCategorized(
            id = 3,
            name = "Invalid Discount",
            discounted = true,
            discountedPercent = -10, // fuera del 0–100
            originalPrice = 1000,
            finalPrice = 200,
            currency = "EUR",
            smallImage = "url",
            discountExpirationNumber = null
        )

        assertEquals(game.originalPrice, game.finalPrice)
        assertEquals(0, game.discountedPercent)
        assertEquals(false, game.discounted)
    }

}
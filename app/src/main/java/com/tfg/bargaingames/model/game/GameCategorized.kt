package com.tfg.bargaingames.model.game

import com.google.gson.annotations.SerializedName
import com.tfg.bargaingames.model.GameItem
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class GameCategorized(
    override val id: Int,
    override val name: String,
    var discounted: Boolean,

//    val type: String,
    @SerializedName("discount_percent")
    var discountedPercent: Int,

    @SerializedName("original_price")
    val originalPrice: Int,

    @SerializedName("final_price")
    var finalPrice: Int,

    val currency: String,

//    @SerializedName("large_capsule_image")
//    val largeImage: String,

    @SerializedName("small_capsule_image")
    val smallImage: String,

    @SerializedName("discount_expiration")
    val discountExpirationNumber: Long?,

    val discountExpiration: LocalDate? = discountExpirationNumber?.let { Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault()).toLocalDate() }

) : GameItem
{
    init {
        inRangeValues()
    }

    fun inRangeValues(){
        if(!discounted){
            discountedPercent = 0
            finalPrice = originalPrice
        }else{
            if(discountedPercent>=100){
                discountedPercent=100
                finalPrice=0
            }else if (discountedPercent<=0){
                discountedPercent = 0
                finalPrice = originalPrice
                discounted = false
            }
        }
    }

    override fun toString(): String {
        return """
        name: $name
        id: $id
        discounted: $discounted
        discountPercent: $discountedPercent
        originalPrice: $originalPrice
        finalPrice: $finalPrice
        currency: $currency
        smallImage: $smallImage
        discountExpirationNumber: $discountExpirationNumber
        discountExpiration: $discountExpiration
    """.trimIndent()
    }
}

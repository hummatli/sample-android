package com.challenge.app.models.remote

import com.challenge.app.models.Beer
import kotlinx.serialization.Serializable

@Serializable
data class BeerRes(
    val id: Int,
    val name: String,
    val abv: String?,
    val description: String?,
    val image_url: String?,
    val food_pairing: List<String>?,
    val ingredients: IngredientRes?
) {
    fun toDomain() = Beer(
        id = id,
        name = name,
        abv = abv,
        description = description,
        imageUrl = image_url,
        foodPairing = food_pairing,
        malt = ingredients?.malt?.map { it.toDomain() },
        hops = ingredients?.hops?.map { it.toDomain() }
    )
}

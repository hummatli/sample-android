package com.challenge.app.models.remote

import kotlinx.serialization.Serializable

@Serializable
data class IngredientRes(
    val malt: List<MaltRes>?,
    val hops: List<HopRes>?
)

package com.challenge.app.models.remote

import com.challenge.app.models.Malt
import kotlinx.serialization.Serializable


@Serializable
data class MaltRes(
    val name: String,
    val amount: AmountRes
) {
    fun toDomain() = Malt(
        name = name,
        amount = amount.toDomain()
    )
}
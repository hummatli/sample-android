package com.challenge.app.models.remote

import com.challenge.app.models.Amount
import kotlinx.serialization.Serializable

@Serializable
data class AmountRes(
    val value: String,
    val unit: String
) {
    fun toDomain() = Amount(
        value = value,
        unit = unit
    )
}

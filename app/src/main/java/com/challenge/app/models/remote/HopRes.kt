package com.challenge.app.models.remote

import com.challenge.app.models.Hop
import com.challenge.app.models.Malt
import kotlinx.serialization.Serializable

@Serializable
data class HopRes(
    val name: String,
    val amount: AmountRes,
    val add: String?,
    val attribute: String?
){
    fun toDomain() = Hop(
        name = name,
        amount = amount.toDomain(),
        add = add,
        attribute = attribute
    )
}
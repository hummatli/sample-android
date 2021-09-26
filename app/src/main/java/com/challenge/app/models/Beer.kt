package com.challenge.app.models

import android.os.Parcelable
import com.challenge.app.models.remote.IngredientRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Beer(
    val id: Int,
    val name: String,
    val abv: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val foodPairing: List<String>? = null,
    val hops: List<Hop>? = null,
    val malt: List<Malt>? = null,
    var isFavorite: Boolean = false
) : Parcelable

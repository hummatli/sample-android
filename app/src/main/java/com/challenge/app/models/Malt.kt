package com.challenge.app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
data class Malt(
    val name: String,
    val amount: Amount
): Parcelable
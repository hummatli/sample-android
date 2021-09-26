package com.challenge.app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hop(
    val name: String,
    val amount: Amount,
    val add: String? = null,
    val attribute: String? = null
): Parcelable
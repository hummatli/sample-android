package com.challenge.app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Amount(
    val value: String,
    val unit: String
): Parcelable

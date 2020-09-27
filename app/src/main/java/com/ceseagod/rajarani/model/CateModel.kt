package com.ceseagod.rajarani.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize

data class CateModel(
    val title: String = "",
    val id: String = "",
    val image: String = "",
    val posted_by: String = "",
    val type: String = "",
    val created_time: Timestamp? = null
): Parcelable
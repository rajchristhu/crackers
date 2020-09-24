package com.ceseagod.rajarani.model

import com.google.firebase.Timestamp

data class CateModel(
    val title: String = "",
    val id: String = "",
    val image: String = "",
    val posted_by: String = "",
    val type: String = "",
    val created_time: Timestamp? = null
)
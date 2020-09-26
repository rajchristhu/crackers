package com.ceseagod.rajarani.model


import com.google.firebase.Timestamp

data class Cateitemmodel(
    val title: String = "",
    val id: String = "",
    val itemId: String = "",
    val image: String = "",
    val posted_by: String = "",
    val color_code: String = "",
    val stack_count: String = "",
    val price: String = "",
    val created_time: Timestamp? = null
)
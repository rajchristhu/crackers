package com.ceseagod.rajarani.model

import com.ceseagod.rajarani.fragment.orderfoodModel
import com.google.firebase.Timestamp

data class orederModel(
    val address: String = "",
    val totalPrice: String = "",
    val deliveryDuration: String = "",
    val landmark: String = "",
    val shopName: String = "",
    val postedTime: Timestamp? = null,
    val foodItems: ArrayList<orderfoodModel>? = null,
    val status: String = "",
    val deleveryperson: String = "",
    val deleverypersonPhoneno: String = "",
    val adminPhoneno: String = ""


)
package com.ceseagod.showcase.support_page

import com.google.firebase.Timestamp

data class supportmessagemodel(
    val timeStamp: Timestamp? = null,
    val messages: String = "",
    val user_id: String = "",
    val type_one: String = "",
    val type: String = ""
)
package com.ceseagod.showcase.support_page

import com.ceseagod.showcase.support_page.supportmessagemodel
import com.google.firebase.Timestamp

data class supportmodel(
    val id: String = "",
    val status: String = "",
    val des: String = "",
    val uid: String = "",
    val timestamp: Timestamp? = null,
    val message: ArrayList<supportmessagemodel>? = null,
    val type:String=""

)

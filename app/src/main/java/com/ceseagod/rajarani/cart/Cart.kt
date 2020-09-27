package com.ceseagod.rajarani.cart



import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class Cart {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var phoneNumber: String? = null
    var name: String? = null
    var ids: String? = null
    var itemids: String? = null
    var variant: String? = null
    var price = 0
    var originalprize = 0
    var inventory = 0
    var count = 0
    var shopname: String? = null

}
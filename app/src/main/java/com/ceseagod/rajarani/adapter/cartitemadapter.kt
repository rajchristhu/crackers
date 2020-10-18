package com.ceseagod.rajarani.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.cart.Cart
import kotlinx.android.synthetic.main.activity_addcart.*

import kotlinx.android.synthetic.main.cartitemdapter.view.*

class cartitemadapter(
    val it: List<Cart>,
    val s: MutableList<List<Cart>>
) :
    RecyclerView.Adapter<cartitemadapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartitemadapter.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cartitemdapter, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return s.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: cartitemadapter.ViewHolder, position: Int) {
        val data = s[position]
        holder.foodname.text = data[0].name
        holder.itemcou.text = data.size.toString() + " ITEMS"
        holder.itemprice.text = data[0].price.toString() + " ₹"
        holder.itemtotal.text = (data[0].price * data.size).toString() + " ₹"
        holder.imageView15.setOnClickListener {

        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodname = itemView.payname
        val itemcou = itemView.itemcou
        val itemprice = itemView.itemprice
        val imageView15 = itemView.imageView15
        val itemtotal = itemView.itemtotal
    }
}
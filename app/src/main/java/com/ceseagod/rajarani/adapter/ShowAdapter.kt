package com.ceseagod.rajarani.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.mainfolder.AddcartActivity
import com.ceseagod.rajarani.mainfolder.ShowPages
import com.ceseagod.rajarani.model.Cateitemmodel
import kotlinx.android.synthetic.main.show_adapter.view.*
import org.jetbrains.anko.startActivity

class ShowAdapter(
    val showModels: MutableList<Cateitemmodel>,
    val showPages: ShowPages
) :
    RecyclerView.Adapter<ShowAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowAdapter.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.show_adapter, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return showModels.size
    }

    override fun onBindViewHolder(holder: ShowAdapter.ViewHolder, position: Int) {
        var data = showModels[position]
        holder.title.text = data.title
        holder.price.text = data.price
        Glide.with(showPages)
            .load(data.image)
            .into(holder.imageView12)
        try {
            holder.cardview.setCardBackgroundColor(Color.parseColor(data.color_code!!.toString()))
        } catch (e: Exception) {
//            holder.cardview.setCardBackgroundColor(Color.parseColor( "#000"))
            holder.cardview.setCardBackgroundColor(Color.parseColor("#b70505"));

        }
        holder.cardview.setOnClickListener {
            showPages.startActivity<AddcartActivity>("id" to data.id)
        }
        holder.fes.setOnClickListener {
            showPages.startActivity<AddcartActivity>("id" to data.id)
        }


    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.title
        var price = itemView.prices
        var cardview = itemView.cardview
        var imageView12 = itemView.imageView12
        var fes = itemView.fes


    }
}
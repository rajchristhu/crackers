package com.ceseagod.rajarani.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ceseagod.rajarani.LiveActivity
import com.ceseagod.rajarani.Modelsw
import com.ceseagod.rajarani.OrderActivity
import com.ceseagod.rajarani.R
import kotlinx.android.synthetic.main.orderadapt.view.*
import org.jetbrains.anko.startActivity

class orderadapt(
    val cateModel: MutableList<Modelsw>,
    val orderActivity: OrderActivity
) : RecyclerView.Adapter<orderadapt.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): orderadapt.ViewHolder {
        return orderadapt.ViewHolder(
            LayoutInflater.from(orderActivity).inflate(R.layout.orderadapt, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return cateModel.size
    }

    override fun onBindViewHolder(holder: orderadapt.ViewHolder, position: Int) {
        val data = cateModel[position]
        holder.textView43.text = position.toString()
        holder.click.setOnClickListener {
            orderActivity.startActivity<LiveActivity>("id" to data.id)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView43 = view.textView43
        val click = view.click
    }
}
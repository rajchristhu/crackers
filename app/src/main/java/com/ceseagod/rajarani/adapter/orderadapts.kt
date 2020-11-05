package com.ceseagod.rajarani.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ceseagod.rajarani.LiveActivity
import com.ceseagod.rajarani.Modelsw
import com.ceseagod.rajarani.R
import kotlinx.android.synthetic.main.orderadapt.view.*
import org.jetbrains.anko.startActivity

class orderadapts(
    val cateModel: MutableList<Modelsw>,
    val orderActivity: LiveActivity,
    val id: String?
) : RecyclerView.Adapter<orderadapts.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): orderadapts.ViewHolder {
        return orderadapts.ViewHolder(
            LayoutInflater.from(orderActivity).inflate(R.layout.orderadapt, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return cateModel.size
    }

    override fun onBindViewHolder(holder: orderadapts.ViewHolder, position: Int) {
        val data = cateModel[position]
        holder.textView43.text = position.toString()
        holder.click.setOnClickListener {
            orderActivity.startActivity<LiveActivitys>("id" to data.id, "ids" to id)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView43 = view.textView43
        val click = view.click
    }
}
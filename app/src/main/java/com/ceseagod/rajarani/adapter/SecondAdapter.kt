package com.ceseagod.rajarani.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.mainfolder.ShowPages
import com.ceseagod.rajarani.model.CateModel
import kotlinx.android.synthetic.main.second_adapter.view.*
import org.jetbrains.anko.startActivity

class SecondAdapter(
    val activity: FragmentActivity,
    val filters: List<CateModel>
) :
    RecyclerView.Adapter<SecondAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsIMage = itemView.newsIMage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.second_adapter, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return filters.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data=filters[position]
                Glide.with(activity)
                    .load(data.image)
                    .placeholder(R.drawable.place)
                    .into(holder.newsIMage)
        if (position!=0)
        {
            holder.newsIMage.setOnClickListener {
                activity!!.startActivity<ShowPages>("id" to data.id)
//            activity!!.startActivity<AddCategoriesItemActivity>()
            }
        }

    }
}
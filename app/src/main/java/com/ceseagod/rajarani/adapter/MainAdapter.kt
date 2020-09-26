package com.ceseagod.rajarani.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.mainfolder.AddCategoriesActivity
import com.ceseagod.rajarani.mainfolder.AddCategoriesItemActivity
import com.ceseagod.rajarani.mainfolder.ShowPages
import com.ceseagod.rajarani.model.CateModel
import kotlinx.android.synthetic.main.main_adapter.view.*
import org.jetbrains.anko.startActivity

class MainAdapter(
    val activity: FragmentActivity,
   val filter: List<CateModel>
) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.textView
        val cardView5 = itemView.cardView5
        val newsIMage = itemView.newsIMage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.main_adapter, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return filter.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data=filter[position]
        Glide.with(activity)
            .load(data.image)
            .into(holder.newsIMage)

        holder.textView.text = data.title
        holder.newsIMage.setOnClickListener {
            activity!!.startActivity<ShowPages>("id" to data.id)
//            activity!!.startActivity<AddCategoriesItemActivity>()
        }
    }
}
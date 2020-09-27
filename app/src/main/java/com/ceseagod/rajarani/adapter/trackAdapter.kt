package com.ceseagod.rajarani.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.model.orederModel

import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.trackadapter.view.*
import java.text.SimpleDateFormat
import java.util.*

class trackAdapter(
    val mutableList: MutableList<orederModel>,
    val context: Context
) : RecyclerView.Adapter<trackAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): trackAdapter.ViewHolder {
        return trackAdapter.ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.trackadapter, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }

    override fun onBindViewHolder(holder: trackAdapter.ViewHolder, position: Int) {
        val data = mutableList[position]
        if (data.status == "open") {
            holder.shopname.text = "Preparing"
            holder.image.setAnimation("preparet.json")

            holder.image.playAnimation()
            holder.image.loop(true)
        } else {
            holder.shopname.text = "On the way"
            holder.image.setAnimation("oncome.json")

            holder.image.playAnimation()
            holder.image.loop(true)

        }
        holder.price.text = data.totalPrice + " ₹"
        val sfd = getcurrentDateTime(data.postedTime!!)
//        sfd.format(Date(data.postedTime.toString()))
        holder.date.text = sfd.toString()

        var re = arrayListOf<String>()
        for (i in data.foodItems!!) {
            re.add(i.foodName + " × " + i.count + ", ")

        }
        val stringArray = re.map { it.toString() }.toTypedArray()

        holder.item.text = re.joinToString()
        holder.delpar.text = data.deleveryperson
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val shopname = itemView.shopname
        val price = itemView.price
        val date = itemView.date
        val item = itemView.item
        val delpar = itemView.delpar
        val image = itemView.image
    }

    private fun getcurrentDateTime(datee: Timestamp): String? {
        val milliseconds = datee.seconds * 1000 + datee.nanoseconds / 1000000
        val sdf = SimpleDateFormat("dd-MMMM-yyyy")
        val netDate = Date(milliseconds)
        val date = sdf.format(netDate)
        return date
    }
}
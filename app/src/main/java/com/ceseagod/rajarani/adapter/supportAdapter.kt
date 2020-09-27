package com.ceseagod.rajarani.adapter

import android.content.Context
import android.graphics.Color
import java.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.mainfolder.SupportChatActivity
import com.ceseagod.showcase.support_page.supportmessagemodel
import com.ceseagod.showcase.support_page.supportmodel
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.supportadapter.view.*
import org.jetbrains.anko.startActivity
import java.util.*
import kotlin.collections.ArrayList

class supportAdapter(
    val supportList: MutableList<supportmodel>,
    val context: Context,
    val supportmsgList: ArrayList<supportmessagemodel>?
) : RecyclerView.Adapter<supportAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.supportadapter, parent, false))
    }

    override fun getItemCount(): Int {
        return supportList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = supportList[position]
        val posts = supportmsgList
        val date=getcurrentDateTime(post.timestamp!!)
        holder.times.text = date
        holder.des.text = post.des
        if (post.status == "closed") {
            holder.back.setBackgroundColor(Color.parseColor("#F1F2F4"))
            holder.times.setTextColor(Color.parseColor("#757882"))
            holder.des.setTextColor(Color.parseColor("#757882"))
        }

        holder.back.setOnClickListener {
            context.startActivity<SupportChatActivity>(
                "id" to post.id,
                "status" to post.status
            )
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val times = view.time
        val back = view.baku
        val des = view.dessa
    }
    private fun getcurrentDateTime(trader_joindate: Timestamp): String? {
        val milliseconds = trader_joindate.seconds * 1000 + trader_joindate.nanoseconds / 1000000
        val sdf = SimpleDateFormat("dd-MMMM-yyyy")
        val netDate = Date(milliseconds)
        val date = sdf.format(netDate)
        return date
    }
}
package com.ceseagod.rajarani.adapter

import android.app.Dialog
import android.app.ProgressDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.mainfolder.SupportChatActivity
import com.ceseagod.showcase.support_page.supportmessagemodel
import com.ceseagod.showcase.support_page.supportmodel
import com.ceseagod.showcase.utilities.SessionMaintainence
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.chatadapt.view.*
import kotlinx.android.synthetic.main.imagefullview.*

class supchatscreenadapter(
    val ress: supportmodel,
    val mwss: ArrayList<supportmessagemodel>?,
    val supportChatActivity: SupportChatActivity
) :
    RecyclerView.Adapter<supchatscreenadapter.MyView>() {
    var firestoreDB: FirebaseFirestore? = null
    val instance = SessionMaintainence.instance
    var progress: ProgressDialog? = null
    val TYPE_ONE = 1
    val TYPE_TWO = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        firestoreDB = FirebaseFirestore.getInstance()
        return when (viewType) {
            TYPE_ONE -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chatadapttwo, parent, false)
                MyView(itemView)

            }
            TYPE_TWO -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chatadapt, parent, false)
                MyView(itemView)
            }
            else -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chatadapt, parent, false)
                MyView(itemView)
            }
        }

    }

    override fun getItemCount(): Int {
        return mwss!!.size
    }

    override fun getItemViewType(position: Int): Int {
        val chatt = ress.message!![position]
        return when {
            chatt.type == "support" -> TYPE_ONE
            chatt.type == "user" -> TYPE_TWO
            else -> TYPE_TWO
        }
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        val chatt = mwss!![position]
        if (chatt.type_one != "text") {
            holder.tet.visibility = View.GONE
            holder.uiy.visibility = View.VISIBLE
            Glide.with(supportChatActivity)
                .load(chatt.messages)
                .placeholder(R.drawable.fiduiconsss)
                .into(holder.uiy)
            holder.uiy.setOnClickListener {
                imageDialog(chatt.messages)
            }
        } else {
            holder.messages.text = chatt.messages
        }

    }

    fun imageDialog(pic: String) {
        val dialogws = Dialog(supportChatActivity, R.style.DialogTheme)
        dialogws.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogws.setContentView(R.layout.imagefullview)
        dialogws.setTitle("My Custom Dialog")
        dialogws.setCancelable(false)
        Glide.with(supportChatActivity)
            .load(pic)
            .placeholder(R.drawable.fiduiconsss)
            .into(dialogws.imhg)

        dialogws.closeeees.setOnClickListener {
            dialogws.dismiss()
        }
        dialogws.show()
    }
    inner class MyView(view: View) : RecyclerView.ViewHolder(view) {
        val messages = view.messages
        val tet = view.tet
        val uiy = view.uiy

    }
}
package com.ceseagod.rajarani.adapter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.mainfolder.AddCategoriesItemActivity
import com.ceseagod.rajarani.mainfolder.AddShopActivity
import com.ceseagod.rajarani.mainfolder.ShopEditActivity
import com.ceseagod.rajarani.mainfolder.ShopItemEditActivity
import com.ceseagod.rajarani.model.Cateitemmodel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.cateadminadapt.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.util.*

class Cateitemadapt(
    val cateModel: MutableList<Cateitemmodel>,
    val activity: AddShopActivity
) :
    RecyclerView.Adapter<Cateitemadapt.ViewHolder>() {
    val inputFormat = "HH:mm"

    private var date: Date? = null
    private var dateCompareOne: Date? = null
    private var dateCompareTwo: Date? = null
    var firestoreDB: FirebaseFirestore? = null

    private var compareStringOne = ""
    private var compareStringTwo = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Cateitemadapt.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cateadminadapt, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return cateModel.size
    }

    override fun onBindViewHolder(holder: Cateitemadapt.ViewHolder, position: Int) {
        val data = cateModel[position]

        holder.editt.setOnClickListener {
            val intent = Intent(activity!!, ShopItemEditActivity::class.java)
            var bundle = Bundle()
            bundle.putParcelable("selected_person", data)
            intent.putExtra("myBundle", bundle)
            ContextCompat.startActivity(activity!!, intent, bundle)
        }
//        holder.kadaiclick.setOnClickListener {
//            activity!!.startActivity<AddCategoriesItemActivity>("id" to data.id)
//        }
        Glide.with(activity!!)
            .load(data.image)
            .into(holder.payimages)
        holder.res.text = data.title
//        if (data.type == "non") {
//            Glide.with(activity)
//                .load(R.drawable.vegg)
//                .into(holder.menu)
//
//        } else if (data.type == "veg") {
//            Glide.with(activity)
//                .load(R.drawable.nonv)
//                .into(holder.menu)
//        }
        firestoreDB = FirebaseFirestore.getInstance()

        holder.price.text = "Time: "
        holder.dele.setOnClickListener {
            Log.e("vr",data.id)
            activity!!.alert( "Categories Name:"+data.title,"Are you sure delete this ?") {
                yesButton {
                    firestoreDB!!.collection("categoriesitem").document(data.id)
                        .delete()
                        .addOnSuccessListener {
                            activity!!.toast("deleted successfully")
                            activity!!.finish();
                            activity!!.startActivity(activity!!.getIntent());
                        }
                        .addOnFailureListener {
                            activity!!.toast("Failed")

                        }
                }
            }.show()
        }

    }



    fun dated(replace: String, replace1: String): Boolean {
        val from = replace.toFloat() * 100
        val to = replace1.toFloat() * 100
        val date = Date()
        val c = Calendar.getInstance()
        c.time = date
        val t = c[Calendar.HOUR_OF_DAY] * 100 + c[Calendar.MINUTE]
        val isBetween =
            to > from && t >= from && t <= to || to < from && (t >= from || t <= to)
        return isBetween
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val res = itemView.payname
        val menu = itemView.menuitem
        val price = itemView.price
        val foodtype = itemView.foodtype
        val type = itemView.type
        val kadaiclick = itemView.kadaiclick
        val textView14 = itemView.textView14
        val editt = itemView.editt
        val dele = itemView.dele
        val view11 = itemView.view11
        val payimages = itemView.payimages
    }
}


package com.ceseagod.rajarani.adapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ceseagod.rajarani.Modelsw
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.model.orederModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_live_activitys.*

class LiveActivitys : AppCompatActivity() {
    var id=""
    var ids=""
    var firestoreDB: FirebaseFirestore? = null
    var cateModel: MutableList<Modelsw> = mutableListOf<Modelsw>()
    var orderList: MutableList<orederModel> = mutableListOf<orederModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_activitys)
        id = intent.getStringExtra("id")
        ids = intent.getStringExtra("ids")
        firestoreDB = FirebaseFirestore.getInstance()
        firestoreDB!!.collection("order").document(ids).collection("mine").document(id).get()
            .addOnSuccessListener {

                    val  s = it.toObject(orederModel::class.java)
                    cateModel.add(Modelsw("1".toString(), it.id))
                    orderList.add(s!!)

                swe.layoutManager = LinearLayoutManager(this)
                swe.adapter = deliveryadapters(this,orderList,cateModel)
//                delrey!!.adapter = deliveryadapters(
//                    this@DelsessionActivity,
//                    so as MutableList<orederModel>
//                )
            }
    }
}
package com.ceseagod.rajarani

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.ceseagod.rajarani.adapter.orderadapt
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_order.*

class OrderActivity : AppCompatActivity() {
    var firestoreDB: FirebaseFirestore? = null
    var cateModel: MutableList<Modelsw> = mutableListOf<Modelsw>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        firestoreDB = FirebaseFirestore.getInstance()
        firestoreDB!!.collection("order").get()
            .addOnSuccessListener { documents ->

                for (doc in documents) {
                    Log.e("DSf",doc.id)
                    Log.d("fg", "${doc.id} => ${doc.data}")

                    cateModel.add(Modelsw(documents.size().toString(), doc.id))
                }
                tkj.layoutManager = LinearLayoutManager(this)
                tkj.adapter = orderadapt(cateModel, this)
            }

    }
}
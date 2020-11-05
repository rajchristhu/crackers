package com.ceseagod.rajarani

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ceseagod.rajarani.adapter.orderadapts
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_live.*
import kotlinx.android.synthetic.main.activity_order.*

class LiveActivity : AppCompatActivity() {
    var id=""
    var firestoreDB: FirebaseFirestore? = null
    var cateModel: MutableList<Modelsw> = mutableListOf<Modelsw>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live)
        id = intent.getStringExtra("id")
        firestoreDB = FirebaseFirestore.getInstance()
        firestoreDB!!.collection("order").document(id).collection("mine").get()
            .addOnSuccessListener {
                for (i in it) {
                    cateModel.add(Modelsw(it.size().toString(), i.id))
                }
                sdwef.layoutManager = LinearLayoutManager(this)
                sdwef.adapter = orderadapts(cateModel, this,id)
            }
    }
}
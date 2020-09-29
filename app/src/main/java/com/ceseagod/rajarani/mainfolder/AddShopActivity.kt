package com.ceseagod.rajarani.mainfolder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.adapter.Cateitemadapt
import com.ceseagod.rajarani.model.Cateitemmodel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_admins.*
import org.jetbrains.anko.startActivity

class AddShopActivity : AppCompatActivity() {
    var firestoreDB: FirebaseFirestore? = null
    var cateModel: MutableList<Cateitemmodel> = mutableListOf<Cateitemmodel>()
    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shop)
        id = intent.getStringExtra("id")

        firestoreDB = FirebaseFirestore.getInstance()
        firestoreDB!!.collection("categoriesitem").get()
            .addOnSuccessListener {
                cateModel.clear()
                for (i in it) {
                    val s = i.toObject(Cateitemmodel::class.java)
                    if (s.itemId == id) {
                        cateModel.add(s)
                    }
                }
                foodadminrec!!.layoutManager = LinearLayoutManager(this)
                foodadminrec!!.adapter = Cateitemadapt(cateModel, this)
            }
            .addOnFailureListener {

            }
        addshops.setOnClickListener {
            startActivity<AddCategoriesItemActivity>("id" to id)

        }
    }
}
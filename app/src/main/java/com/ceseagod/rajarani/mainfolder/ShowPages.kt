package com.ceseagod.rajarani.mainfolder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.adapter.ShowAdapter
import com.ceseagod.rajarani.model.Cateitemmodel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_show_pages.*

class ShowPages : AppCompatActivity() {
    var firestoreDB: FirebaseFirestore? = null
    var showModel: Cateitemmodel? = null
    var showModels = mutableListOf<Cateitemmodel>()
    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_pages)
        firestoreDB = FirebaseFirestore.getInstance()
        id = try {
            intent.getStringExtra("id")
        } catch (e: Exception) {
            ""
        }

        firestoreDB!!.collection("categoriesitem")
            .orderBy("created_time", Query.Direction.DESCENDING).get()
            .addOnSuccessListener {
                showModels.clear()
                for (i in it) {
                    showModel = i.toObject(Cateitemmodel::class.java)
                    try {
                        if (id == showModel!!.itemId) {
                            showModels.add(showModel!!)
                        }
                    } catch (e: Exception) {
                    }
                }
                val acceptHorizontalLayoutsss =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                mainre!!.layoutManager = acceptHorizontalLayoutsss
                mainre!!.adapter = ShowAdapter(showModels,this)

            }
            .addOnFailureListener {

            }


    }
}
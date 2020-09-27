package com.ceseagod.rajarani.mainfolder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.adapter.Cateadapt
import com.ceseagod.rajarani.model.CateModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_admins.*
import org.jetbrains.anko.startActivity

class AdminsActivity : AppCompatActivity() {
    var firestoreDB: FirebaseFirestore? = null
    var cateModel: MutableList<CateModel> = mutableListOf<CateModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admins)
        firestoreDB = FirebaseFirestore.getInstance()
        firestoreDB!!.collection("kadai").get()
            .addOnSuccessListener {
                cateModel.clear()
                for (i in it) {
                    val s = i.toObject(CateModel::class.java)
                    cateModel.add(s)
                }
                foodadminrec!!.layoutManager = LinearLayoutManager(this)
                foodadminrec!!.adapter = Cateadapt(cateModel, this)
            }
            .addOnFailureListener {

            }



        addshop.setOnClickListener {
            startActivity<AddCategoriesActivity>()
        }
        addshops.setOnClickListener {
            startActivity<AddCategoriesActivity>()
        }
    }

    }

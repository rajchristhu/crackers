package com.ceseagod.rajarani.mainfolder

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.model.Cateitemmodel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_addcart.*


class AddcartActivity : AppCompatActivity() {
    var firestoreDB: FirebaseFirestore? = null
    var id = ""
    var showModel: Cateitemmodel? = null
    var showModels = mutableListOf<Cateitemmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addcart)
        firestoreDB = FirebaseFirestore.getInstance()
        id = try {
            intent.getStringExtra("id")
        } catch (e: Exception) {
            ""
        }
        firestoreDB!!.collection("categoriesitem").document(id)
            .get()
            .addOnSuccessListener {
                showModel = it.toObject(Cateitemmodel::class.java)

                Glide.with(this)
                    .load(showModel!!.image)
                    .into(imageView10)
            }
            .addOnFailureListener {

            }
    }
}
package com.ceseagod.rajarani.mainfolder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.adapter.ShowAdapter
import com.ceseagod.rajarani.cart.Cart
import com.ceseagod.rajarani.fragment.MainViewModel
import com.ceseagod.rajarani.model.Cateitemmodel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_show_pages.*
import org.jetbrains.anko.startActivity

class ShowPages : AppCompatActivity() {
    var firestoreDB: FirebaseFirestore? = null
    var showModel: Cateitemmodel? = null
    var showModels = mutableListOf<Cateitemmodel>()
    var id = ""
    var s: Int? = null

    private lateinit var viewModel: MainViewModel
    var cartList: MutableList<Cart> = mutableListOf<Cart>()
    var lis: ArrayList<String> = arrayListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_pages)
        firestoreDB = FirebaseFirestore.getInstance()
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

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
                    view()
                }
                val acceptHorizontalLayoutsss =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                mainre!!.layoutManager = acceptHorizontalLayoutsss
                mainre!!.adapter = ShowAdapter(showModels,this)

            }
            .addOnFailureListener {

            }
        imageView7.setOnClickListener {
            startActivity<CartItemActivity>()
            finish()

        }

    }

    private fun view() {
        viewModel?.allWords?.observe(this,
            Observer<List<Cart>> { t ->
                cartList.clear()
                for (i in t) {
                    cartList.add(i)
                }


                if (t.isNotEmpty()) {
                    var price = 0
                    for (i in t) {
                        price += i.price.toInt()
                    }
                    prices.text = "$price ₹"
                }

            })
    }

}
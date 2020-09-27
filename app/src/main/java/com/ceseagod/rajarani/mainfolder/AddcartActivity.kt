package com.ceseagod.rajarani.mainfolder

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.cart.Cart
import com.ceseagod.rajarani.fragment.MainViewModel
import com.ceseagod.rajarani.model.Cateitemmodel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_addcart.*
import kotlinx.android.synthetic.main.activity_addcart.imageView7
import kotlinx.android.synthetic.main.activity_addcart.prices
import kotlinx.android.synthetic.main.activity_show_pages.*
import org.jetbrains.anko.startActivity


class AddcartActivity : AppCompatActivity() {
    var firestoreDB: FirebaseFirestore? = null
    var id = ""
    var s: Int? = null

    private lateinit var viewModel: MainViewModel
    var cartList: MutableList<Cart> = mutableListOf<Cart>()
    var lis: ArrayList<String> = arrayListOf<String>()

    var showModel: Cateitemmodel? = null
    var showModels = mutableListOf<Cateitemmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addcart)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        firestoreDB = FirebaseFirestore.getInstance()
        id = try {
            intent.getStringExtra("id")
        } catch (e: Exception) {
            ""
        }

        imageView7.setOnClickListener {
            startActivity<CartItemActivity>()
            finish()
        }

        firestoreDB!!.collection("categoriesitem").document(id)
            .get()
            .addOnSuccessListener {
                showModel = it.toObject(Cateitemmodel::class.java)

                Glide.with(this)
                    .load(showModel!!.image)
                    .into(imageView10)
                view()
            }
            .addOnFailureListener {

            }

        number_picker.setOnValueChangeListener(ElegantNumberButton.OnValueChangeListener { view, oldValue, newValue ->
            val number: String = number_picker.number
            s = newValue - oldValue
            val cart = Cart()
            if (newValue == 0) {
                butts.visibility = View.VISIBLE
                number_picker.visibility = View.INVISIBLE
            }
            if (s == 1) {

                cart.phoneNumber = "9597"
                cart.variant = ""
                cart.name = showModel!!.title
                cart.ids = showModel!!.id
                cart.itemids = showModel!!.itemId
                cart.count = number.toInt()
                cart.shopname = "name"
                cart.price = showModel!!.price.toInt()
                cart.originalprize = showModel!!.stack_count.toInt()
                if (cartList.size == 0) {
                    viewModel!!.insert(cart)
                } else {
                    viewModel!!.insert(cart)
                }
            } else {

                cart.phoneNumber = "9597"
                cart.variant = ""
                cart.name = showModel!!.title
                cart.ids = showModel!!.id
                cart.itemids = showModel!!.itemId
                cart.count = oldValue.toInt()
                cart.shopname = "name"
                cart.price = showModel!!.price.toInt()
                cart.originalprize = showModel!!.stack_count.toInt()
                viewModel!!.deletePerticular(cart)
            }
        })



        butts.setOnClickListener {
            checkAlready(showModel)
            number_picker.number = "1"
        }
    }

    private fun view() {
        viewModel?.allWords?.observe(this,
            Observer<List<Cart>> { t ->
                cartList.clear()
                for (i in t) {
                    cartList.add(i)
                }
                var no = cartList.filter { it.ids == showModel!!.id }
                lis.clear()
                for (i in cartList) {
                    lis.add(i.ids!!)
                }

                if (lis.contains(showModel!!.id)) {
                    number_picker!!.visibility = View.VISIBLE
                    butts.visibility = View.INVISIBLE
                } else {
                    number_picker.visibility = View.INVISIBLE
                    butts.visibility = View.VISIBLE
                }
                number_picker.number = no.size.toString()

                if (t.isNotEmpty()) {
                    var price = 0
                    for (i in t) {
                        price += i.price.toInt()
                    }
                    prices.text = "$price ₹"
                }

            })
    }

    fun checkAlready(data: Cateitemmodel?) {
        val cart = Cart()
        cart.phoneNumber = "9597"
        cart.variant = ""
        cart.name = data!!.title
        cart.ids = data.id
        cart.itemids = data.itemId
        cart.count = 1
        cart.shopname = "name"
        cart.price = data.price.toInt()
        cart.originalprize = data.stack_count.toInt()
        if (cartList.size == 0) {
            viewModel!!.insert(cart)
        } else {
//            if (ides == cartList[0].itemids) {
            viewModel!!.insert(cart)
//            }
//            else {
//                cartList.clear()
//                viewModel!!.delete(cart)
//            }
        }

    }
}
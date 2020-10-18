package com.ceseagod.rajarani.mainfolder

import android.graphics.Paint
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
            if (prices.text.toString() != "0 ₹") {
                startActivity<CartItemActivity>()
                finish()
            }

        }
        imageView9.setOnClickListener {
            finish()
        }
        firestoreDB!!.collection("categoriesitem").document(id)
            .get()
            .addOnSuccessListener {
                showModel = it.toObject(Cateitemmodel::class.java)
                if (showModel!!.stack_count.toInt() <= 3) {
                    textView30.visibility = View.VISIBLE
                    butts.visibility = View.INVISIBLE
                    number_picker.visibility = View.INVISIBLE
                } else {
                    textView30.visibility = View.GONE
                    butts.visibility = View.VISIBLE
                    number_picker.visibility = View.VISIBLE
                }
                Glide.with(this)
                    .load(showModel!!.image)
                    .into(imageView10)
                var price = (showModel!!.price.toInt() * 50) / 100
                textView15.text = showModel!!.title
                textView20.text = showModel!!.price + " ₹"
                textView24.text = " 50% off"
                textView22.text = "$price  ₹"
                textView20.paintFlags = textView20.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                view()
            }
            .addOnFailureListener {

            }

        number_picker.setOnValueChangeListener(ElegantNumberButton.OnValueChangeListener { view, oldValue, newValue ->
            val number: String = number_picker.number
            s = newValue - oldValue
            val cart = Cart()
            if (showModel!!.stack_count.toInt() >= 3) {

                if (newValue == 0) {
                    butts.visibility = View.VISIBLE
                    number_picker.visibility = View.INVISIBLE
                }
            }
            if (s == 1) {

                cart.phoneNumber = "9597"
                cart.variant = ""
                cart.name = showModel!!.title
                cart.ids = showModel!!.id
                cart.itemids = showModel!!.itemId
                cart.count = number.toInt()
                cart.shopname = "name"
                cart.price = (showModel!!.price.toInt() * 50) / 100
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
                cart.price = (showModel!!.price.toInt() * 50) / 100
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
                if (showModel!!.stack_count.toInt() >= 3) {

                    if (lis.contains(showModel!!.id)) {
                        number_picker!!.visibility = View.VISIBLE
                        butts.visibility = View.INVISIBLE
                    } else {
                        if (showModel!!.stack_count.toInt() >= 3) {
                            number_picker.visibility = View.INVISIBLE
                            butts.visibility = View.VISIBLE
                        }
                    }
                }
                number_picker.number = no.size.toString()

                if (t.isNotEmpty()) {
                    var price = 0
                    for (i in t) {
                        price += i.price.toInt()
                    }
                    prices.text = "$price ₹"
                } else {
                    prices.text = "0 ₹"

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
        cart.price = (data!!.price.toInt() * 50) / 100
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
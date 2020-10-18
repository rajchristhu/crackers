package com.ceseagod.rajarani.mainfolder

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ceseagod.rajarani.MainActivity
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.adapter.cartitemadapter
import com.ceseagod.rajarani.cart.Cart
import com.ceseagod.rajarani.customview.dialog
import com.ceseagod.rajarani.fragment.MainViewModel
import com.ceseagod.showcase.utilities.SessionMaintainence
import com.google.firebase.firestore.FirebaseFirestore
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_cart_item.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.jetbrains.anko.startActivity
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class CartItemActivity : AppCompatActivity(), CoroutineScope {
    val acceptHorizontalLayouts =
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    var progress: ProgressDialog? = null
    var sa = ""
    private var mWordViewModel: MainViewModel? = null
    var sse = ""
    var swe = ""
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()
    var toStrings = ""
    val cart = mutableListOf<List<Cart>>()
    var price = 0
    var firestoreDB: FirebaseFirestore? = null

    var oprice = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_item)
        mWordViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        imageView4.setOnClickListener {
            finish()
        }

        button2.setOnClickListener {
            startActivity<MainActivity>()

        }
        mWordViewModel?.allWords?.observe(this, androidx.lifecycle.Observer {
            cart_recyclerView!!.layoutManager = acceptHorizontalLayouts
            val s = it.groupBy { it.ids }
            cart.clear()
            s.values.forEach {
                cart.add(it)
            }

            for (i in cart) {
                price += i[0].price * i.size
                oprice += i[0].originalprize * i.size
                sse += i[0].name + ", "
            }
            val dis = ((price * 2) / 100)
            val del = ((price * 10) / 100)
            val to = dis + del
            dicountprice.text = "${dis.toString()} ₹"
            deliveryprice.text = "${del.toString()} ₹"
            textView19.text = "- $to ₹"
            totalprice.text = "$price ₹"
            textView29.text = sse
//            dicountprice.text = "- " + (oprice - price).toString()
            toStrings = (price).toString()
            totalprices.text = "$toStrings ₹"
            cart_recyclerView!!.adapter = cartitemadapter(it, cart)
        })
        pay.setOnClickListener {
            if (SessionMaintainence.instance!!.is_loggedin) {
                if (toStrings.toInt() >= 1500) {
                    startActivity<AddressActivity>("price" to toStrings)
                } else {
                    showDialogs()
                }
//                bottomSheetshow(toStrings, cart, price, oprice)
            } else {
                showDialog()
            }
        }
    }

    private fun showDialog() {
        val fragmentManager = this!!.supportFragmentManager
        val newFragment = dialog()
        val transaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit()
    }

    private fun showDialogs() {
        val fragmentManager = this!!.supportFragmentManager
        val newFragment = dialogs()
        val transaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit()
    }

}
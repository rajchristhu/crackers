package com.ceseagod.rajarani.mainfolder

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.ceseagod.rajarani.MainActivity
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.cart.Cart
import com.ceseagod.rajarani.fragment.MainViewModel
import com.ceseagod.rajarani.fragment.orderfoodModel
import com.ceseagod.rajarani.model.orederModel
import com.ceseagod.showcase.utilities.SessionMaintainence
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_address.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class AddressActivity : AppCompatActivity() {
    var price = ""
    private var mWordViewModel: MainViewModel? = null
    val cart = mutableListOf<List<Cart>>()
    var firestoreDB: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        price = try {
            intent.getStringExtra("price")
        } catch (e: Exception) {
            ""
        }
        firestoreDB = FirebaseFirestore.getInstance()

        mWordViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mWordViewModel?.allWords?.observe(this, androidx.lifecycle.Observer {
            val s = it.groupBy { it.ids }
            cart.clear()
            s.values.forEach {
                cart.add(it)
            }
        })

        pay.setOnClickListener {
            val input1 = inputs1.text.toString()
            val input1a = inputs1a.text.toString()
            val input1b = inputs1b.text.toString()
            val input1c = inputs1c.text.toString()
            val input1d = inputs1d.text.toString()
            val input1e = inputs1e.text.toString()
            val s = checksValidation(input1, input1a, input1b, input1c, input1d, input1e)
            if (s) {
                order(input1, input1a, input1b, input1c, input1d, input1e)
            } else {
                toast("Fill All details")
                if (inputs1.text.toString().isEmpty()) {
                    inputs1.error = "Fill the details"
                }
                if (inputs1a.text.toString().isEmpty()) {
                    inputs1a.error = "Fill the details"

                }
                if (inputs1b.text.toString().isEmpty()) {
                    inputs1b.error = "Fill the details"
                }
                if (inputs1c.text.toString().isEmpty()) {
                    inputs1c.error = "Fill the details"
                }
//            if (inputs1d.text.toString().isEmpty()) {
//                inputs1d.error = "Fill the details"
//            }
                if (inputs1e.text.toString().isEmpty()) {
                    inputs1e.error = "Fill the details"
                }
            }
        }


    }

    private fun order(
        input1: String,
        input1a: String,
        input1b: String,
        input1c: String,
        input1d: String,
        input1e: String
    ) {
        val ed = arrayListOf<orderfoodModel>()
        for (i in cart) {
            ed.add(
                orderfoodModel(
                    i[0].name.toString(),
                    i[0].itemids.toString(),
                    i[0].price.toString(),
                    i[0].originalprize.toString(),
                    i.size.toString()
                )
            )
        }
        val model = orederModel(
            ("$input1, $input1a, $input1b, $input1c, $input1e"),
            price,
            "With in 4 days", input1d, "Sivakasi Crackers Factory",
            Timestamp.now(), ed, "open", "", "",
            "9488042014"
        )
        image.visibility = View.VISIBLE
        image.setAnimation("pre.json")
        image.playAnimation()
        val doc = firestoreDB!!.collection("order")
            .document(SessionMaintainence.instance!!.Uid!!)
            .collection("mine")
            .add(model)
        doc.addOnSuccessListener {
            image.visibility = View.GONE
            mWordViewModel!!.deleteall()
            startActivity<MainActivity>()
        }
        doc.addOnFailureListener {
            image.visibility = View.GONE

        }

    }

    private fun checksValidation(
        topti: String,
        imagePath: String?,
        toptitwo: String,
        des: String,
        input4: String,
        input1e: String
    ): Boolean {
        return topti != "" && imagePath != "" && toptitwo != "" && des != "" && input1e != ""
    }

}
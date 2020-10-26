package com.ceseagod.rajarani.mainfolder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.ceseagod.rajarani.MainActivity
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.cart.Cart
import com.ceseagod.rajarani.fragment.MainViewModel
import com.ceseagod.rajarani.fragment.orderfoodModel
import com.ceseagod.rajarani.model.orederModel
import com.ceseagod.rajarani.utilities.SessionMaintainence
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_address.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONObject

class AddressActivity : AppCompatActivity(), PaymentResultListener {
    var price = ""
    private var mWordViewModel: MainViewModel? = null
    val cart = mutableListOf<List<Cart>>()
    var firestoreDB: FirebaseFirestore? = null
    private var chackout: Checkout? = null
    private var razorpayKey: String? = null
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
        imageView4.setOnClickListener {
            finish()

        }

        pay.setOnClickListener {
            val input1 = inputs1.text.toString()
            val input1a = inputs1a.text.toString()
            val input1b = inputs1b.text.toString()
            val input1c = inputs1c.text.toString()
            val input1d = inputs1d.text.toString()
            val input1e = inputs1e.text.toString()
            val s = checksValidation(input1, input1a, input1b, input1c, input1d, input1e)
            if (s) {

                //you have to convert Rs. to Paisa using multiplication of 100
                val convertedAmount: String =
                    (price.toInt() * 100).toString()

                rezorpayCall(
                    SessionMaintainence.instance!!.firstName,
                    "",
                    SessionMaintainence.instance!!.phoneno,
                    convertedAmount
                )
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

    override fun onPaymentError(p0: Int, p1: String?) {
        toast("fail")
    }

    override fun onPaymentSuccess(p0: String?) {
        val input1 = inputs1.text.toString()
        val input1a = inputs1a.text.toString()
        val input1b = inputs1b.text.toString()
        val input1c = inputs1c.text.toString()
        val input1d = inputs1d.text.toString()
        val input1e = inputs1e.text.toString()
        order(input1, input1a, input1b, input1c, input1d, input1e)

    }

    fun rezorpayCall(
        name: String?,
        email: String?,
        phNo: String?,
        convertedAmount: String?
    ) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        razorpayKey =
            "rzp_live_tTUgbwN0dn32ME" //Generate your razorpay key from Settings-> API Keys-> copy Key Id
        chackout = Checkout()
        chackout!!.setKeyID(razorpayKey)
        try {
            val options = JSONObject()
            options.put("name", name)
            options.put("description", "Payment")
            options.put("currency", "INR")
            options.put("amount", convertedAmount)
            val preFill = JSONObject()
            preFill.put("email", email)
            preFill.put("contact", phNo)
            options.put("prefill", preFill)
            chackout!!.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_LONG)
                .show()
            e.printStackTrace()
        }
    }
}
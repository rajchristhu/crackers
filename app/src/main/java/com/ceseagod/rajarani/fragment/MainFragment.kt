package com.ceseagod.rajarani.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ceseagod.rajarani.MainActivity
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.adapter.MainAdapter
import com.ceseagod.rajarani.adapter.SecondAdapter
import com.ceseagod.rajarani.cart.Cart
import com.ceseagod.rajarani.mainfolder.CartItemActivity
import com.ceseagod.rajarani.model.CateModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.imageView7
import kotlinx.android.synthetic.main.main_fragment.prices
import org.jetbrains.anko.startActivity


class MainFragment(val mainActivity: MainActivity) : Fragment() {
    var cateModel: CateModel? = null
    var cateModels= mutableListOf<CateModel>()

    companion object {
//        fun newInstance() = MainFragment(this)
    }
    var firestoreDB: FirebaseFirestore? = null
    var cartList: MutableList<Cart> = mutableListOf<Cart>()

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        firestoreDB=FirebaseFirestore.getInstance()

        imageView7.setOnClickListener {
            if (prices.text.toString()!="0 ₹")
            {
                mainActivity.startActivity<CartItemActivity>()
                mainActivity.finish()
            }

        }
        try {
            imaged.visibility = View.VISIBLE
            imaged.setAnimation("pre.json")

            imaged.playAnimation()
            imaged.loop(true)
        } catch (e: Exception) {
        }
        try {
//            activity!!.itemcard.visibility = View.GONE
//
//            activity!!.main.visibility = View.GONE
//            activity!!.sub.visibility = View.VISIBLE
            activity!!.equal_navigation_bars.visibility = View.VISIBLE
//            activity!!.head.text="Support"
        } catch (e: Exception) {
        }
        imaged.visibility=View.VISIBLE
        firestoreDB!!.collection("categories").orderBy("created_time", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { document ->
                cateModels.clear()
                for (i in document)
                {
                    cateModel = i.toObject(CateModel::class.java)
                    cateModels!!.add(cateModel!!)
                    val filter=cateModels!!.filter { it.type=="main" }
                    val filters=cateModels!!.filter { it.type=="sub" }
                    val acceptHorizontalLayoutsss =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                    try {
                        recyclerViewone!!.layoutManager = acceptHorizontalLayoutsss
                        recyclerViewone!!.adapter = MainAdapter( activity!!,filter)
                        recyclerViewtwo!!.layoutManager =   LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                        recyclerViewtwo!!.adapter = SecondAdapter( activity!!,filters)
                    } catch (e: Exception) {
                    }

                }
                view()
                imaged.visibility=View.GONE

            }
            .addOnFailureListener {
                imaged.visibility=View.GONE

//                    toppy.visibility=View.GONE
            }


    }
    private fun view()
    {
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
                else
                {
                    prices.text = "0 ₹"

                }

            })
    }
}
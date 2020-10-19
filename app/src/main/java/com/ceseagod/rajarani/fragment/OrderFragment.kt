package com.ceseagod.rajarani.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.adapter.orderAdapter
import com.ceseagod.rajarani.model.orederModel

import com.ceseagod.rajarani.utilities.SessionMaintainence
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.order_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class OrderFragment : Fragment() {
    private var mWordViewModel: MainViewModel? = null

    var sessionMaintainence = SessionMaintainence.instance
    var firestoreDB: FirebaseFirestore? = null
    var orderList: MutableList<orederModel> = mutableListOf<orederModel>()
    var count = ""

    companion object {
        fun newInstance() = OrderFragment()
    }

    private lateinit var viewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        orderList.clear()
        viewModel = ViewModelProviders.of(this).get(OrderViewModel::class.java)
        firestoreDB = FirebaseFirestore.getInstance()
        mWordViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        try {
//            activity!!.itemcard.visibility = View.GONE
//
//            activity!!.main.visibility = View.GONE
//            activity!!.sub.visibility = View.VISIBLE
            activity!!.equal_navigation_bars.visibility = View.GONE
//            activity!!.head.text="Support"
        } catch (e: Exception) {
        }
//        try {
//            activity!!.itemcard.visibility = View.GONE
//
//            activity!!.main.visibility = View.GONE
//            activity!!.sub.visibility = View.VISIBLE
//            activity!!.equal_navigation_bars.visibility = View.GONE
//            activity!!.head.text = "Order History"
//        } catch (e: Exception) {
//        }
        val sdf = SimpleDateFormat("MMM dd,yyyy")
        val currentDateandTime: String = sdf.format(Date())

        val sdfs = SimpleDateFormat("EEEE")
        val currentDateandTimes: String = sdfs.format(Date())
        day.text = currentDateandTimes
        date.text = currentDateandTime
        firestoreDB!!.collection("order").document(SessionMaintainence.instance!!.Uid!!)
            .collection("mine")
            .get()
            .addOnSuccessListener {
                for (i in it) {
                    val s = i.toObject(orederModel::class.java)
                    orderList.add(s)
                }
                var monthList: List<orederModel> = orderList.filter { s -> s.status == "close" }
                count = monthList.size.toString()
                orderco.text = count
                orderrec.layoutManager = LinearLayoutManager(context)
                orderrec.adapter = orderAdapter(monthList as MutableList<orederModel>, context!!)
            }
            .addOnFailureListener {

            }
    }

    override fun onStart() {
        super.onStart()
        orderList.clear()
    }
}
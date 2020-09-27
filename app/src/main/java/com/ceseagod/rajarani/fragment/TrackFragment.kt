package com.ceseagod.rajarani.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.adapter.trackAdapter
import com.ceseagod.rajarani.model.orederModel
import com.ceseagod.showcase.utilities.SessionMaintainence
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.track_fragment.*

class TrackFragment : Fragment() {

    var firestoreDB: FirebaseFirestore? = null
    var orderList: MutableList<orederModel> = mutableListOf<orederModel>()
    companion object {
        fun newInstance() = TrackFragment()
    }

    private lateinit var viewModel: TrackViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.track_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TrackViewModel::class.java)
        firestoreDB = FirebaseFirestore.getInstance()

//        try {
//            activity!!.itemcard.visibility = View.GONE
//
//            activity!!.main.visibility = View.GONE
//            activity!!.sub.visibility = View.VISIBLE
//            activity!!.equal_navigation_bars.visibility = View.GONE
//            activity!!.head.text = "Track Order"
//        } catch (e: Exception) {
//        }


        firestoreDB!!.collection("order").document(SessionMaintainence.instance!!.Uid!!)
            .collection("mine")
            .get()
            .addOnSuccessListener {
                orderList.clear()
                for (i in it) {
                    val s = i.toObject(orederModel::class.java)
                    orderList.add(s)
                }
                try {
                    var monthList: List<orederModel> = orderList.filter { s -> s.status != "close" }

                    trackor.layoutManager = LinearLayoutManager(context)
                    trackor.adapter = trackAdapter(monthList as MutableList<orederModel>, context!!)
                } catch (e: Exception) {
                }
            }
            .addOnFailureListener {

            }
    }
    override fun onStart() {
        super.onStart()
        orderList.clear()
    }






}
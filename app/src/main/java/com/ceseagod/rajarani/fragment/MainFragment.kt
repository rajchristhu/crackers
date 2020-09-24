package com.ceseagod.rajarani.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.adapter.MainAdapter
import com.ceseagod.rajarani.adapter.SecondAdapter
import com.ceseagod.rajarani.model.CateModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment() {
    var cateModel: CateModel? = null
    var cateModels= mutableListOf<CateModel>()

    companion object {
        fun newInstance() = MainFragment()
    }
    var firestoreDB: FirebaseFirestore? = null

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
        firestoreDB!!.collection("categories").orderBy("created_time", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { document ->
                for (i in document)
                {
                    cateModel = i.toObject(CateModel::class.java)
                    cateModels!!.add(cateModel!!)
                    val filter=cateModels!!.filter { it.type=="main" }
                    val filters=cateModels!!.filter { it.type=="sub" }
                    val acceptHorizontalLayoutsss =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                    recyclerViewone!!.layoutManager = acceptHorizontalLayoutsss
                    recyclerViewone!!.adapter = MainAdapter( activity!!,filter)
                    recyclerViewtwo!!.layoutManager =   LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                    recyclerViewtwo!!.adapter = SecondAdapter( activity!!,filters)

                }
            }
            .addOnFailureListener {
//                    toppy.visibility=View.GONE
            }


    }

}
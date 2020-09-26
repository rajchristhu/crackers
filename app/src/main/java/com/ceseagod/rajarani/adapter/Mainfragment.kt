package com.ceseagod.rajarani.adapter

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ceseagod.rajarani.R

class Mainfragment : Fragment() {

    companion object {
        fun newInstance() = Mainfragment()
    }

    private lateinit var viewModel: MainfragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.mainfragment_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainfragmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
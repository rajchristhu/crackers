package com.ceseagod.rajarani.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ceseagod.rajarani.R

class AdminMainFragment : Fragment() {

    companion object {
        fun newInstance() = AdminMainFragment()
    }

    private lateinit var viewModel: AdminMainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.admin_main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AdminMainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
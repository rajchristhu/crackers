package com.ceseagod.rajarani.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ceseagod.rajarani.R

class CategorieMainFragment : Fragment() {

    companion object {
        fun newInstance() = CategorieMainFragment()
    }

    private lateinit var viewModel: CategorieMainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.categorie_main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CategorieMainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
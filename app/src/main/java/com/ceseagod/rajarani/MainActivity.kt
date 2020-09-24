package com.ceseagod.rajarani

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ceseagod.rajarani.fragment.MainFragment
import com.ceseagod.showcase.utilities.SessionMaintainence
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var firestoreDB: FirebaseFirestore? = null
    var drawerLayouts: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayouts = drawerLayout
        firestoreDB = FirebaseFirestore.getInstance()
        equal_navigation_bars.setNavigationChangeListener { view, position ->
            when (position) {
                2 -> {
                    changeFragment(MainFragment(), "profile")
                }
                0 -> {
                    changeFragment(MainFragment(), "mainfrag")
                }
                1 -> {
                    changeFragment(MainFragment(), "grocery")
                }


                //navigation changed, do something here
            }


            //navigation changed, do something here
        }
        equal_navigation_bars.setCurrentActiveItem(0)
        menuss.setOnClickListener {
            drawerLayouts!!.openDrawer(drawer)
        }
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, drawerLayouts,
            1, 0
        ) {
            private val scaleFactor = 10f

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                val slideX = drawerView.width * slideOffset
                content.translationX = slideX
                content.scaleX = 1 - slideOffset / scaleFactor
                content.scaleY = 1 - slideOffset / scaleFactor

//                profilenames.text = sessionMaintainence!!.firstName
//                Glide.with(userimage.context)
//                    .load(sessionMaintainence!!.profilepic)
//                    .placeholder(R.drawable.imjhk)
//                    .into(userimage)
            }
        }
        drawerLayouts!!.addDrawerListener(actionBarDrawerToggle)
        drawerLayouts!!.setScrimColor(Color.TRANSPARENT)
        drawerLayouts!!.drawerElevation = 0f

    }
    private fun changeFragment(targetFragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment2, targetFragment, tag)
            .addToBackStack(null)
            //            .setTransitionStyle(FragmentTransaction.TRANSIT_NONE)
            .commit()
    }

}
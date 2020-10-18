package com.ceseagod.rajarani

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ceseagod.rajarani.cart.Cart
import com.ceseagod.rajarani.fragment.*
import com.ceseagod.showcase.utilities.SessionMaintainence
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    var firestoreDB: FirebaseFirestore? = null
    var drawerLayouts: DrawerLayout? = null
    private var mWordViewModel: MainViewModel? = null

    var cartList: MutableList<Cart> = mutableListOf<Cart>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mWordViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        drawerLayouts = drawerLayout
        firestoreDB = FirebaseFirestore.getInstance()
//        mWordViewModel!!.deleteall()
        changeFragment(MainFragment(this), "mainfrag")

        equal_navigation_bars.setNavigationChangeListener { view, position ->
            when (position) {
                2 -> {
                    changeFragment(ProfileFragment(), "profile")
                }
                0 -> {
                    changeFragment(MainFragment(this), "mainfrag")
                }
                1 -> {
                    changeFragment(TrackFragment(), "grocery")
                }


                //navigation changed, do something here
            }


            //navigation changed, do something here
        }
        if (!SessionMaintainence!!.instance!!.is_loggedin) {
            m_item_photos.visibility = View.GONE
            post.visibility = View.GONE
            job.visibility = View.GONE
            support.visibility = View.GONE
        }
        post.setOnClickListener {
            drawerLayouts!!.closeDrawers()
            changeFragment(OrderFragment(), "order")
        }
        job.setOnClickListener {
            drawerLayouts!!.closeDrawers()
            changeFragment(TrackFragment(), "track")
        }
        support.setOnClickListener {
            drawerLayouts!!.closeDrawers()
            changeFragment(support_frag(this), "support")
        }
        equal_navigation_bars.setCurrentActiveItem(0)
        menuss.setOnClickListener {
            drawerLayouts!!.openDrawer(drawer)
        }
        rate.setOnClickListener {
            rate()
        }
        rates.setOnClickListener {
            startActivity<Sign>()
        }
        terms.setOnClickListener {
            startActivity<Terms>()
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

    override fun onBackPressed() {
        super.onBackPressed()
        try {
            drawerLayouts!!.closeDrawers()
        } catch (e: Exception) {
        }

    }
    fun rate() {
        val uri: Uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }

}
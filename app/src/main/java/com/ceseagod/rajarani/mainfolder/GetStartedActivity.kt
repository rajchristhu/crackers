package com.ceseagod.rajarani.mainfolder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.adapter.SliderAdapter
import kotlinx.android.synthetic.main.activity_get_started.*
import org.jetbrains.anko.startActivity

class GetStartedActivity : AppCompatActivity() {
    var slogan: ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)
        val sliderImage = ArrayList<Int>()
        sliderImage.add(R.drawable.ic_launcher_background)
        sliderImage.add(R.drawable.ic_launcher_background)
        sliderImage.add(R.drawable.ic_launcher_background)

        slogan = arrayListOf<String>("Confident Coding!!", "Explore Many!!", "Make Peaceful Life!!")
        viewPager.adapter = SliderAdapter(this, sliderImage, slogan!!)
        indicator.setupWithViewPager(viewPager, true)

        start.setOnClickListener {
            startActivity<LoginActivity>()
            finish()
        }
    }

    //back stack
    override fun onBackPressed() {
        finish()
        moveTaskToBack(true)
    }
}

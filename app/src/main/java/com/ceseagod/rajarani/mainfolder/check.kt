package com.ceseagod.rajarani.mainfolder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ceseagod.rajarani.MainActivity
import com.ceseagod.rajarani.R
import com.ceseagod.showcase.utilities.SessionMaintainence
import org.jetbrains.anko.startActivity

class check : AppCompatActivity() {
    var sessionMaintainence = SessionMaintainence.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)
        val login = sessionMaintainence!!.is_loggedin
        if (!login) {
            if (sessionMaintainence!!.dans) {
                startActivity<LoginActivity>()
                finish()
            } else {
                startActivity<GetStartedActivity>()
                finish()
            }


        } else {
            if (SessionMaintainence.instance!!.userType == "admin") {
                startActivity<AdminsActivity>()
                finish()
            } else {
                startActivity<MainActivity>()
                finish()
            }

//                startActivity<AdminsActivity>()


        }
//            startActivity(Intent(this, checkActivity::class.java))
        // close this activity
        finish()
    }
}
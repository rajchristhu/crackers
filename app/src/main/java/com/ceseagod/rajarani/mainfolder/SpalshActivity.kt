package com.ceseagod.rajarani.mainfolder

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.ceseagod.rajarani.MainActivity
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.utilities.SessionMaintainence
import org.jetbrains.anko.startActivity

class SpalshActivity : AppCompatActivity() {
    public val SPLASH_TIME_OUT: Long = 2000 // 2 sec
    var sessionMaintainence = SessionMaintainence.instance
    //Hooks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        //Handler for splash
        Handler().postDelayed({
            val login = sessionMaintainence!!.is_loggedin
            if (!login) {
                if (sessionMaintainence!!.dans) {
                    startActivity<LoginActivity>()
                    finish()
                } else {
                    startActivity<LoginActivity>()
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
        }, SPLASH_TIME_OUT)
    }
}
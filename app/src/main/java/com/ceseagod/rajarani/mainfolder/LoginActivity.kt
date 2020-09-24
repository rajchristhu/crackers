package com.ceseagod.rajarani.mainfolder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.ceseagod.rajarani.MainActivity
import com.ceseagod.rajarani.R
import com.ceseagod.showcase.utilities.SessionMaintainence
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {
    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    var slogan: ArrayList<String>? = null

    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (!SessionMaintainence.instance!!.dans) {
            skip.visibility = View.VISIBLE
            view.visibility = View.VISIBLE
            close.visibility = View.INVISIBLE
        } else {
            skip.visibility = View.INVISIBLE
            view.visibility = View.INVISIBLE
            close.visibility = View.VISIBLE
        }
        view.setOnClickListener {
            SessionMaintainence.instance!!.dans = true
            startActivity<MainActivity>()
            finish()

        }
        close.setOnClickListener {
            SessionMaintainence.instance!!.dans = true
            startActivity<MainActivity>()
            finish()

        }
        SessionMaintainence.instance!!.dans = true
        sendotp.setOnClickListener {
            if (phoneno.text.toString().count() == 10) {
                sendotp.text = "Loading..."
                Handler().postDelayed({
                    SessionMaintainence.instance!!.phoneno = phoneno.text.toString()
                    startActivity<OtpScreen>("mobile" to phoneno.text.toString())
                }, 1000)
            }

        }
        phoneno.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().count() == 10) {
                    sendotp.setBackgroundResource(R.drawable.buttonback)
                    sendotp.setTextColor(resources.getColor(R.color.white))
                } else {
                    sendotp.setBackgroundResource(R.drawable.beforebutton)
                    sendotp.setTextColor(resources.getColor(R.color.grays))
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        sendotp.text = "Send OTP"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
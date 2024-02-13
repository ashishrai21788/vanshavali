package com.app.vanshavali.aggrement.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.vanshavali.PreferenceData
import com.app.vanshavali.R
import com.app.vanshavali.home.view.LandingActivity
import com.app.vanshavali.utils.Animatoo
import kotlinx.android.synthetic.main.user_aggreement.*

class UserAgreement : AppCompatActivity() {

    lateinit var prefsHelper: PreferenceData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_aggreement)
        prefsHelper = PreferenceData(UserAgreement@this)
        prefsHelper.setIsAgreementAccepted("true")
        layout_start.setOnClickListener {
            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
            finish()
        }
    }
}
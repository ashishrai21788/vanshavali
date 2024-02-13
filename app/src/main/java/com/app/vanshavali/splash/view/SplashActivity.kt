package com.app.vanshavali.splash.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.app.vanshavali.PreferenceData
import com.app.vanshavali.R
import com.app.vanshavali.aggrement.view.UserAgreement
import com.app.vanshavali.application.RoomApplication
import com.app.vanshavali.home.view.LandingActivity
import com.app.vanshavali.landing.view.ListActivity
import com.app.vanshavali.utils.Animatoo

class SplashActivity : AppCompatActivity() {

    private val ARG_CATEGORY_ID by lazy { "categoryId" }
    private val ARG_CATEGORY_NAME by lazy { "categoryName" }
    private val ARG_CATEGORY_DETAILS by lazy { "categoryDetail" }
    private val ARG_CATEGORY_IMAGE by lazy { "categoryImage" }


    lateinit var prefsHelper: PreferenceData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        prefsHelper = PreferenceData(this@SplashActivity)
        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            try {
                if( prefsHelper.getIsAgreementAccepted() == "false"){
                    val intent = Intent(this, UserAgreement::class.java)
                    startActivity(intent)
                    Animatoo.animateSlideLeft(this)
                    finish()
                }
                else if(prefsHelper.getCategoryId().equals("0")){
                    val intent = Intent(this, LandingActivity::class.java)
                    startActivity(intent)
                    Animatoo.animateSlideLeft(this)
                    finish()
                }else{
                    val intent =
                        Intent(applicationContext, ListActivity::class.java)
                            .putExtra(ARG_CATEGORY_ID, prefsHelper.getCategoryId())
                            .putExtra(ARG_CATEGORY_NAME, prefsHelper.getCategoryName())
                            .putExtra(
                                ARG_CATEGORY_DETAILS,
                                prefsHelper.getCategoryDetails())
                            .putExtra(ARG_CATEGORY_IMAGE, prefsHelper.getCategoryImage())
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Animatoo.animateSlideLeft(this)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
            finish()
        }

    }, 7000) // 3000 is the delayed time in milliseconds.
}
}
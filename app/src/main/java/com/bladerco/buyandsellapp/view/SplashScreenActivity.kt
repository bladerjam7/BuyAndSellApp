package com.bladerco.buyandsellapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.core.os.postDelayed
import com.bladerco.buyandsellapp.R

class SplashScreenActivity : AppCompatActivity() {

    private val SPLASH_SCREEN_TIMEOUT = 5000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))

        }, SPLASH_SCREEN_TIMEOUT)
    }
}
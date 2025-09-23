package com.hin.flixcomix.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import com.hin.flixcomix.R
import com.hin.flixcomix.ui.activity.MainActivity
import com.hin.flixcomix.utils.EActivityOptionAnim
import com.hin.flixcomix.utils.extensions.pushActivity
//import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
//            delay(1000)
            pushActivity(MainActivity::class, EActivityOptionAnim.FADE)
            finish()
        }
    }
}
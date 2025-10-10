package com.hin.movie.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hin.movie.R
import com.hin.movie.ui.activity.MainActivity
import com.hin.movie.ui.auth.AuthActivity
import com.hin.movie.utils.EActivityOptionAnim
import com.hin.movie.utils.extensions.pushActivity
//import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
//            delay(1000)
            pushActivity(AuthActivity::class, EActivityOptionAnim.FADE)
            finish()
        }
    }
}
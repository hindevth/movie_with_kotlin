package com.hin.movie.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
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
        val auth = FirebaseAuth.getInstance()

        lifecycleScope.launch {
            if (auth.currentUser == null){
                pushActivity(AuthActivity::class, EActivityOptionAnim.FADE)
            }else{
                pushActivity(MainActivity::class, EActivityOptionAnim.FADE)
            }
            finish()
        }
    }
}
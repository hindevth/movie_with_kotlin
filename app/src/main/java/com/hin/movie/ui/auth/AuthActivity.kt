package com.hin.movie.ui.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.hin.movie.R
import com.hin.movie.ui.activity.MainActivity
import com.hin.movie.ui.base.BaseActivity
import com.hin.movie.utils.EActivityOptionAnim
import com.hin.movie.utils.extensions.pushActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)

    }

    override fun getNavController(): NavController {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_auth) as NavHostFragment
        val navController = navHostFragment.navController
        return navController
    }

    fun pushHome(){
        pushActivity(MainActivity::class, EActivityOptionAnim.FADE)
        finish()
    }
}
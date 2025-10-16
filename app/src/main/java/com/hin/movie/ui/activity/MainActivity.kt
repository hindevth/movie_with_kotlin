package com.hin.movie.ui.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hin.movie.R
import com.hin.movie.databinding.ActivityMainBinding
import com.hin.movie.ui.base.BaseActivity
import com.hin.movie.utils.backwardAnim
import com.hin.movie.utils.extensions.dpToPx
import com.hin.movie.utils.extensions.gone
import com.hin.movie.utils.extensions.visible
import com.hin.movie.utils.forwardAnim
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
            !isDarkThemeOn()

        onListener()

    }

    fun onListener() {
        val navView: BottomNavigationView = binding.navBottomView
        val navFragment = binding.navHostFragmentActivityMain
        navFragment.setPadding(0, 0, 0, 44.dpToPx(this))

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val tabOrder = setOf(
            R.id.navigation_home,
            R.id.navigation_history,
            R.id.navigation_bookmark,
            R.id.navigation_profile
        )

        navController.addOnDestinationChangedListener { _, des, _ ->
            if (des.id in tabOrder) {
                navFragment.setPadding(0, 0, 0, 44.dpToPx(this))
                navView.visible()
                navView
                    .animate()
                    .translationY(0f)
                    .setDuration(300L)
                    .start()
            } else {
                navFragment.setPadding(0, 0, 0, 0)
                navView
                    .animate()
                    .translationY(navView.height.toFloat())
                    .setDuration(300L)
                    .withEndAction { navView.gone() }
                    .start()
            }
        }

        navView.setOnItemSelectedListener { item ->
            val currentId =
                navController.currentDestination?.id ?: return@setOnItemSelectedListener false

            // Nếu đã ở tab hiện tại thì không làm gì
            if (currentId == item.itemId) {
                return@setOnItemSelectedListener true
            }

            val currentIndex = tabOrder.indexOf(currentId)
            val targetIndex = tabOrder.indexOf(item.itemId)

            // Kiểm tra debug
            if (targetIndex > currentIndex) {
                replaceTo(item.itemId)
            } else {
                replaceTo(item.itemId, null, backwardAnim(getNavController(), true))
            }
            true
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when(navController.currentDestination?.id) {
                    R.id.navigation_home -> finish()
                    R.id.navigation_history,
                    R.id.navigation_bookmark,
                    R.id.navigation_profile -> {
                        replaceTo(R.id.navigation_home, null, backwardAnim(getNavController(), true))
                        navView.selectedItemId = R.id.navigation_home
                    }
                    else -> navController.popBackStack()
                }
            }
        })
    }

    override fun getNavController(): NavController {
        return findNavController(R.id.nav_host_fragment_activity_main)
    }

    fun replaceTo(
        @IdRes resId: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = forwardAnim(getNavController(), true)
    ) {
        val navController = getNavController()
        val options = navOptions ?: NavOptions.Builder()
            .setPopUpTo(navController.graph.startDestinationId, inclusive = true)
            .build()
        navController.navigate(resId, args, options)
    }

    private fun isDarkThemeOn(): Boolean {
        val currentMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentMode == Configuration.UI_MODE_NIGHT_YES
    }

    fun showLoading(){
        binding.frameLayoutLoading.visible()
    }

    fun hideLoading(){
        binding.frameLayoutLoading.gone()
    }
}
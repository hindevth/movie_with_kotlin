package com.hin.flixcomix.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hin.flixcomix.R
import com.hin.flixcomix.databinding.ActivityMainBinding
import com.hin.flixcomix.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onListener()

    }

    fun onListener() {
        val navView: BottomNavigationView = binding.navBottomView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val tabOrder = setOf(
            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
        )

        navController.addOnDestinationChangedListener { _, des, _ ->
            if (des.id in tabOrder) {
                binding.navBottomView.visibility = View.VISIBLE
            } else {
                binding.navBottomView.visibility = View.GONE
            }
        }

        navView.setOnItemSelectedListener { item ->
            val currentId = navController.currentDestination?.id

            val currentIndex = tabOrder.indexOf(currentId)
            val targetIndex = tabOrder.indexOf(item.itemId)


            val navOptions = if (currentIndex < targetIndex) {

                NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setRestoreState(true)
                    .setPopUpTo(navController.graph.startDestinationId, false, saveState = true)
                    .setEnterAnim(R.anim.slide_in_right)
                    .setExitAnim(R.anim.slide_out_left)
                    .setPopEnterAnim(R.anim.slide_in_left)
                    .setPopExitAnim(R.anim.slide_out_right)
                    .build()
            } else {
                NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setRestoreState(true)
                    .setPopUpTo(navController.graph.startDestinationId, false, saveState = true)
                    .setEnterAnim(R.anim.slide_in_left)
                    .setExitAnim(R.anim.slide_out_right)
                    .setPopEnterAnim(R.anim.slide_in_right)
                    .setPopExitAnim(R.anim.slide_out_left)
                    .build()
            }

            navController.navigate(item.itemId, null, navOptions)
            true
        }

//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                if (navController.currentDestination?.id in tabOrder) {
//                    navView.selectedItemId = R.id.navigation_home
//                }
//            }
//        })
    }

    override fun getNavController(): NavController {
        return findNavController(R.id.nav_host_fragment_activity_main)
    }

    fun setDrawerContent(layoutRes: ViewBinding) {
        binding.drawerContainer.removeAllViews()
        binding.drawerContainer.addView(layoutRes.root)
    }

    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.END)
    }

    fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.END)
    }
}
package com.hin.movie.utils

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.hin.movie.R

fun forwardAnim(navController: NavController? = null, inclusive: Boolean = true): NavOptions {
    val nav = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)

    navController?.let {
        nav.setPopUpTo(it.graph.id, inclusive)
    }
    return nav.build()
}

fun backwardAnim(navController: NavController? = null, inclusive: Boolean = true): NavOptions {
    val nav = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_left)
        .setExitAnim(R.anim.slide_out_right)
        .setPopEnterAnim(R.anim.slide_in_right)
        .setPopExitAnim(R.anim.slide_out_left)

    navController?.let {
        nav.setPopUpTo(it.graph.id, inclusive)
    }
    return nav.build()
}

fun forwardAnimFade(navController: NavController? = null, inclusive: Boolean = true): NavOptions {
    val nav = NavOptions.Builder()
        .setEnterAnim(R.anim.fade_in)
        .setExitAnim(R.anim.fade_out)
        .setPopEnterAnim(R.anim.fade_in)
        .setPopExitAnim(R.anim.fade_out)

    navController?.let {
        nav.setPopUpTo(it.graph.id, inclusive)
    }
    return nav.build()
}
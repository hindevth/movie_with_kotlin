package com.hin.flixcomix.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.core.graphics.drawable.toDrawable

fun dialog(context: Context, @LayoutRes layoutResID: Int): Dialog {
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(layoutResID)

    // setup window
    dialog.window?.apply {
        setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        setGravity(Gravity.CENTER)
    }

    return dialog
}
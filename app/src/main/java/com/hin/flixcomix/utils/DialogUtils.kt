package com.hin.flixcomix.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.core.graphics.drawable.toDrawable
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hin.flixcomix.R

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

fun bottomSheetDialog(context: Context, @LayoutRes layoutResID: Int): BottomSheetDialog {
    val bottomSheet = BottomSheetDialog(context)
    bottomSheet.setContentView(layoutResID)
    bottomSheet.setOnShowListener { d ->
        val bott = (d as BottomSheetDialog)
            .findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bott?.setBackgroundColor(Color.TRANSPARENT)
    }
    return bottomSheet
}
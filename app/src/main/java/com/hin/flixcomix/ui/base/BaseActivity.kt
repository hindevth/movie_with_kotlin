package com.hin.flixcomix.ui.base

import android.graphics.Rect
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.hin.flixcomix.R
import com.hin.flixcomix.utils.EActivityOptionAnim

abstract class BaseActivity : AppCompatActivity() {
    override fun finish() {
        super.finish()
        val anim = intent.getStringExtra("ANIM_ACTIVITY") ?: EActivityOptionAnim.SLIDE.name
        when (anim) {
            EActivityOptionAnim.STACK.name -> overridePendingTransition(0, R.anim.slide_out_right)
            EActivityOptionAnim.SLIDE.name -> overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )

            EActivityOptionAnim.FADE.name -> overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            else -> overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    // Ẩn bàn phím
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    abstract fun getNavController(): NavController
}
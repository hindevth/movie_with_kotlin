package com.hin.flixcomix.ui.custom.image_view

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatImageView
import com.hin.flixcomix.R
import androidx.core.view.isVisible

class SpinImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val spinAnimation = AnimationUtils.loadAnimation(context, R.anim.spin_infinity)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isVisible){
            startAnimation(spinAnimation)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearAnimation()
    }

    fun show(){
        visibility = VISIBLE
        startAnimation(spinAnimation)
    }

    fun hide(){
        clearAnimation()
        visibility = GONE
    }
}
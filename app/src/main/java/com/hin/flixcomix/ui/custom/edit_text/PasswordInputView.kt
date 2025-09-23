package com.hin.flixcomix.ui.custom.edit_text

import android.content.Context
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.widget.doAfterTextChanged
import com.hin.flixcomix.R
import com.hin.flixcomix.databinding.LayoutPasswordInputBinding

class PasswordInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding: LayoutPasswordInputBinding

    init {
        binding = LayoutPasswordInputBinding.inflate(LayoutInflater.from(context), this, true)
        setupPasswordToggle()
    }

    private fun setupPasswordToggle() {
        var passwordVisible = false
        binding.etPassword.apply {
            setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    val editText = v as EditText
                    val drawableEnd = editText.compoundDrawablesRelative[2] ?: return@setOnTouchListener false
                    val touchArea = editText.width - editText.paddingEnd - drawableEnd.bounds.width()
                    if (event.x >= touchArea) {
                        passwordVisible = !passwordVisible
                        editText.transformationMethod = if (passwordVisible) {
                            HideReturnsTransformationMethod.getInstance()
                        } else {
                            PasswordTransformationMethod.getInstance()
                        }
                        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            R.drawable.lock_bold,
                            0,
                            if (passwordVisible) R.drawable.show_bold else R.drawable.hide_bold,
                            0
                        )
                        editText.setSelection(editText.text.length)
                        editText.isHapticFeedbackEnabled = false
                        editText.performClick()
                        return@setOnTouchListener true
                    }
                }
                false
            }
        }
    }

    fun doAfterTextChanged(action: (String) -> Unit) {
        binding.etPassword.doAfterTextChanged {
            action(it?.toString().orEmpty())
        }
    }

    fun getText(): String = binding.etPassword.text.toString()

    fun setText(value: String) {
        binding.etPassword.setText(value)
    }
}
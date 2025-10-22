@file:JvmName("BindingAdapters")

package com.hin.movie.ui.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hin.movie.ui.custom.edit_text.PasswordInputView

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(view)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }else{
        Glide.with(view).clear(view)
        view.setImageDrawable(null)
    }
}

@BindingAdapter("appDebugText")
fun debugText(view: TextView, value: String?) {
    view.text = value ?: "null"
}

@BindingAdapter("passwordHint")
fun setPasswordHint(view: PasswordInputView, hint: String = "") {
    view.setHint(hint)
}

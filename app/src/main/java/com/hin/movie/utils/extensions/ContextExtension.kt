package com.hin.movie.utils.extensions

import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityOptionsCompat
import com.hin.movie.R
import com.hin.movie.utils.EActivityOptionAnim
import kotlin.reflect.KClass
import java.io.Serializable

fun Context.pushActivity(
    cls: KClass<*>,
    anim: EActivityOptionAnim?,
    vararg params: Pair<String, Any?>,
) {
    val intent = Intent(this, cls.java)
    intent.putExtra("ANIM_ACTIVITY", anim?.name)
    // add params vào intent
    for ((key, value) in params) {
        when (value) {
            null -> intent.putExtra(key, null as Serializable?)
            is Int -> intent.putExtra(key, value)
            is Long -> intent.putExtra(key, value)
            is CharSequence -> intent.putExtra(key, value)
            is String -> intent.putExtra(key, value)
            is Float -> intent.putExtra(key, value)
            is Double -> intent.putExtra(key, value)
            is Char -> intent.putExtra(key, value)
            is Short -> intent.putExtra(key, value)
            is Boolean -> intent.putExtra(key, value)
            is Serializable -> intent.putExtra(key, value)
            else -> throw IllegalArgumentException("Unsupported param type: ${value::class}")
        }
    }

    val option = when (anim) {
        EActivityOptionAnim.FADE -> ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.fade_in,
            R.anim.fade_out
        )

        EActivityOptionAnim.STACK -> ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.slide_in_right,
            0
        )

        EActivityOptionAnim.SLIDE -> ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )

        else -> ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }
    startActivity(intent, option.toBundle())
}
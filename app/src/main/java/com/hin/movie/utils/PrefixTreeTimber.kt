package com.hin.movie.utils

import android.util.Log
import timber.log.Timber

class PrefixTree() : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val level = when (priority) {
            Log.ERROR -> "❌"
            Log.WARN -> "⚠️"
            Log.INFO -> "ℹ️"
            else -> "👉"
        }
        super.log(priority, tag, "---------- $level $message", t)
    }
}
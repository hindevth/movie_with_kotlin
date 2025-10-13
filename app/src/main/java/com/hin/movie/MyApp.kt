package com.hin.movie

import android.app.Application
import com.google.firebase.FirebaseApp
import com.hin.movie.utils.PrefixTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(PrefixTree())
        }
    }
}
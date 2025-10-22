package com.hin.movie

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.gson.Gson
import com.hin.movie.data.entities.Setting
import com.hin.movie.data.local.SettingDataSource
import com.hin.movie.utils.Constants
import com.hin.movie.utils.PrefixTree
import com.hin.movie.utils.helper.LocaleHelper
import com.hin.movie.utils.helper.ThemeHelper
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

    override fun attachBaseContext(base: Context?) {
        val sharedPref = base?.getSharedPreferences(Constants.CACHE_KEY, MODE_PRIVATE)
        val setting = sharedPref?.getString("setting", null)?.let {
            Gson().fromJson(it, Setting::class.java)
        } ?: Setting("auto", "auto")

        val newContext = LocaleHelper.setLocale(base!!, setting.language)
        ThemeHelper.applyTheme(setting.theme)
        super.attachBaseContext(newContext)
    }
}
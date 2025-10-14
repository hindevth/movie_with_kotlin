package com.hin.movie

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.hin.movie.data.local.SettingDataSource
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
        val repo = SettingDataSource(base)
        val setting = repo.getSetting()
        val newContext = LocaleHelper.setLocale(base!!, setting.language)
        ThemeHelper.applyTheme(setting.theme)
        super.attachBaseContext(newContext)
    }
}
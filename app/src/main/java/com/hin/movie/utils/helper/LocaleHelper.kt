package com.hin.movie.utils.helper

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

object LocaleHelper {
    fun setLocale(context: Context, language: String): Context {
        val locale = when(language) {
            "vi" -> Locale.forLanguageTag(language)
            "en" -> Locale.forLanguageTag(language)
            else -> Locale.getDefault()
        }

        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    fun changeAppLanguage(languageCode: String) {
        AppCompatDelegate.setApplicationLocales(
            androidx.core.os.LocaleListCompat.forLanguageTags(languageCode)
        )
    }
}
package com.hin.movie.data.local

import android.content.Context
import com.google.gson.Gson
import com.hin.movie.data.entities.Setting
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class SettingDataSource(context: Context?) {
    private val sharedPreferences = context?.getSharedPreferences("setting_data", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveSetting(setting: Setting) {
        val settingJson = gson.toJson(setting)
        sharedPreferences?.edit { putString("setting", settingJson) }
    }

    fun getSetting(): Setting {
        return sharedPreferences?.getString("setting", null)?.let {
            gson.fromJson(it, Setting::class.java)
        } ?: Setting("auto", "auto")
    }

    fun clearSetting() {
        sharedPreferences?.edit { remove("setting") }
    }
}
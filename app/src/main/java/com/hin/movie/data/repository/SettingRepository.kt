package com.hin.movie.data.repository

import com.hin.movie.data.entities.Setting
import com.hin.movie.data.local.SettingDataSource
import javax.inject.Inject

class SettingRepository @Inject constructor(private val settingDataSource: SettingDataSource) {
    fun updateSetting(setting: Setting){
        settingDataSource.saveSetting(setting)
    }

    fun getSetting() = settingDataSource.getSetting()
}
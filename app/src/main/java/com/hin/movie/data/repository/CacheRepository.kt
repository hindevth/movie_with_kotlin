package com.hin.movie.data.repository

import com.hin.movie.data.entities.Setting
import com.hin.movie.data.entities.User
import com.hin.movie.data.local.SettingDataSource
import com.hin.movie.data.local.UserDataSource
import javax.inject.Inject

class CacheRepository @Inject constructor(
    private val settingDataSource: SettingDataSource,
    private val userDataSource: UserDataSource
) {

    fun updateSetting(setting: Setting){
        settingDataSource.saveSetting(setting)
    }

    fun getSetting() = settingDataSource.getSetting()

    fun clearSetting() = settingDataSource.clearSetting()

    fun saveUser(user: User) {
        userDataSource.saveUser(user)
    }

    fun getUser(): User? {
        return userDataSource.getUser()
    }

    fun clearUser() = userDataSource.clearUser()
}


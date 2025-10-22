package com.hin.movie.data.local

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.hin.movie.data.entities.User
import com.hin.movie.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataSource @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences = context.getSharedPreferences(Constants.CACHE_KEY, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        Timber.e(userJson)
        sharedPreferences?.edit {
            putString("user", userJson)
        }
    }

    fun getUser(): User? {
        return sharedPreferences?.getString("user", null)?.let {
            Timber.i(it)
            gson.fromJson(it, User::class.java)
        }
    }

    fun clearUser() {
        sharedPreferences?.edit {
            remove("user")
        }
    }

}
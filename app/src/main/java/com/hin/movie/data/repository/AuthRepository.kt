package com.hin.movie.data.repository

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.hin.movie.data.remote.firebase.AuthService
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authService: AuthService) {

    suspend fun loginGoogle(activity: Activity): Result<FirebaseUser?> {
        return authService.loginGoogle(activity)
    }

    suspend fun loginWithPassword(email: String, password: String): Result<FirebaseUser?> {
        return authService.login(email, password)
    }

    suspend fun registerWithPassword(email: String, password: String): Result<FirebaseUser?> {
        return authService.register(email, password)
    }

    suspend fun updateProfile(name: String?, avatar: String?) {
        authService.updateProfile(name, avatar)
    }

    suspend fun getCurrentUser() = authService.getCurrentUser()

    suspend fun logout() = authService.logout()
}
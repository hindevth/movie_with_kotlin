package com.hin.movie.ui.sigin_in_social

import android.app.Activity
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.hin.movie.data.repository.AuthRepository
import com.hin.movie.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SignInSocialViewModel @Inject constructor(private val authRepository: AuthRepository) :
    BaseViewModel() {

    fun signInWithGoogle(activity: Activity) {
        viewModelScope.launch {
            val data = authRepository.loginGoogle(activity)
            data.onSuccess { firebaseUser ->
                Timber.i(firebaseUser?.email)
            }.onFailure {
                Timber.e(it)
            }
        }
    }
}
package com.hin.movie.ui.sigin_in_social

import android.app.Activity
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.hin.movie.data.repository.AuthRepository
import com.hin.movie.ui.base.BaseViewModel
import com.hin.movie.ui.sigin_in_social.data.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SignInSocialViewModel @Inject constructor(private val authRepository: AuthRepository) :
    BaseViewModel() {
    private val _uiState = MutableStateFlow<UIState>(UIState())
    val uiState: StateFlow<UIState> = _uiState

    fun clearMessageError(){
        _uiState.value = _uiState.value.copy(messageError = null)
    }

    fun signInWithGoogle(activity: Activity) {
        _isLoading.value = true
        viewModelScope.launch {
            val data = authRepository.loginGoogle(activity)
            data.onSuccess { firebaseUser ->
                Timber.i(firebaseUser?.email)
                _uiState.value = _uiState.value.copy(isLoginSuccess = true)
            }.onFailure {
                Timber.e(it)
                _uiState.value =
                    _uiState.value.copy(isLoginSuccess = false, messageError = it.message)
                _isLoading.value = false
            }
        }
    }
}
package com.hin.movie.ui.sign_in_password

import android.app.Activity
import androidx.lifecycle.viewModelScope
import com.hin.movie.data.repository.AuthRepository
import com.hin.movie.ui.base.BaseViewModel
import com.hin.movie.ui.sign_in_password.data.UIState
import com.hin.movie.utils.extensions.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SignInPasswordViewModel @Inject constructor(private val authRepository: AuthRepository) :
    BaseViewModel() {
    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email, isEmailValid = email.isValidEmail())
        validButton()
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, isPasswordValid = password.length > 4)
        validButton()
    }

    fun validButton() {
        val enable = uiState.value.isEmailValid && uiState.value.isPasswordValid
        _uiState.value = _uiState.value.copy(isButtonEnabled = enable)
    }

    fun clearMessageError() {
        _uiState.value = _uiState.value.copy(messageError = null)
    }


    fun signIn() {
        _isLoading.value = true
        viewModelScope.launch {
            val data =
                authRepository.loginWithPassword(uiState.value.email!!, uiState.value.password!!)
            data.onSuccess {
                _uiState.value = _uiState.value.copy(isLoginSuccess = true)
            }.onFailure { exception ->
                Timber.e(exception)
                _uiState.value = _uiState.value.copy(messageError = exception.message, isLoginSuccess = false)
                _isLoading.value = false
            }
        }
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
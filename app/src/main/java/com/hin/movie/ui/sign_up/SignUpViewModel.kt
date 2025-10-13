package com.hin.movie.ui.sign_up

import android.app.Activity
import androidx.lifecycle.viewModelScope
import com.hin.movie.data.remote.firebase.AuthService
import com.hin.movie.data.repository.AuthRepository
import com.hin.movie.ui.base.BaseViewModel
import com.hin.movie.ui.sign_up.data.UIState
import com.hin.movie.utils.extensions.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SignUpViewModel @Inject constructor(private val authRepository: AuthRepository) : BaseViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email, isEmailValid = email.isValidEmail())
        validButton()
    }

    fun updatePassword(password: String) {
        _uiState.value =
            _uiState.value.copy(password = password, isPasswordValid = password.length > 4)
        validButton()
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(
            confirmPassword = confirmPassword,
            isConfirmPasswordValid = confirmPassword == uiState.value.password
        )
        validButton()
    }

    fun validButton() {
        val enable =
            uiState.value.isEmailValid && uiState.value.isPasswordValid && uiState.value.isConfirmPasswordValid
        _uiState.value = _uiState.value.copy(isButtonSignUpEnabled = enable)
    }

    fun clearMessageError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearRegisterSuccess() {
        _uiState.value = _uiState.value.copy(isRegisterSuccess = false)
    }


    fun signUp() {
        _isLoading.value = true
        viewModelScope.launch {
            val data = authRepository.registerWithPassword(_uiState.value.email, _uiState.value.password)
            data
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(isRegisterSuccess = true)
                }
                .onFailure { exception ->
                    _uiState.value =
                        _uiState.value.copy(error = exception.message, isRegisterSuccess = false)
                    Timber.e(exception)
                }

            _isLoading.value = false
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
                    _uiState.value.copy(isLoginSuccess = false, error = it.message)
                _isLoading.value = false
            }
        }
    }
}
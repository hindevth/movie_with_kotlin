package com.hin.movie.ui.sign_in_password.data

data class UIState(
    val email: String? = null,
    val password: String? = null,
    val isLoginSuccess: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isButtonEnabled: Boolean = false,
    val messageError: String? = null
)

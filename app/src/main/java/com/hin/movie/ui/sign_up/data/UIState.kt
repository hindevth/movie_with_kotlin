package com.hin.movie.ui.sign_up.data

data class UIState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isConfirmPasswordValid: Boolean = false,
    val isButtonSignUpEnabled: Boolean = false,
    val isRegisterSuccess: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val error: String? = null
)

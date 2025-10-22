package com.hin.movie.ui.profile

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hin.movie.data.entities.User
import com.hin.movie.data.repository.AuthRepository
import com.hin.movie.data.repository.CacheRepository
import com.hin.movie.ui.base.BaseViewModel
import com.hin.movie.ui.profile.data.UIState
import com.hin.movie.utils.extensions.toUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val cacheRepository: CacheRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        _user.value = cacheRepository.getUser()
        _uiState.value = _uiState.value.copy(setting = cacheRepository.getSetting())
    }

    fun updateTheme(theme: String) {
        val newSetting = uiState.value.setting!!.copy(theme = theme)
        cacheRepository.updateSetting(newSetting)
        _uiState.value = _uiState.value.copy(setting = newSetting)
    }

    fun updateLanguage(language: String) {
        val newSetting = uiState.value.setting!!.copy(language = language)
        cacheRepository.updateSetting(newSetting)
        _uiState.value = _uiState.value.copy(setting = newSetting)
    }

    fun signOut() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                authRepository.logout()
                _uiState.value = _uiState.value.copy(isLogoutSuccess = true)
            } catch (e: Exception) {
                Timber.e(e)
                _isLoading.value = false
            }

        }
    }
}
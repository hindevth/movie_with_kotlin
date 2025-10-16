package com.hin.movie.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hin.movie.data.entities.User
import com.hin.movie.data.repository.AuthRepository
import com.hin.movie.data.repository.SettingRepository
import com.hin.movie.ui.base.BaseViewModel
import com.hin.movie.ui.profile.data.UIState
import com.hin.movie.utils.extensions.toUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseAuth: FirebaseAuth,
    private val settingRepository: SettingRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        _user.value = firebaseAuth.currentUser?.toUser()
        _uiState.value = _uiState.value.copy(setting = settingRepository.getSetting())
    }

    fun updateTheme(theme: String){
        val newSetting = uiState.value.setting!!.copy(theme = theme)
        settingRepository.updateSetting(newSetting)
        _uiState.value = _uiState.value.copy(setting = newSetting)
    }

    fun updateLanguage(language: String){
        val newSetting = uiState.value.setting!!.copy(language = language)
        settingRepository.updateSetting(newSetting)
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
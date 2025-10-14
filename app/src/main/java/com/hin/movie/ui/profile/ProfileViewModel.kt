package com.hin.movie.ui.profile

import androidx.lifecycle.viewModelScope
import com.hin.movie.data.repository.AuthRepository
import com.hin.movie.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(private val authRepository: AuthRepository) : BaseViewModel() {

    fun signOut(){
        _isLoading.value = true
        viewModelScope.launch {
            authRepository.logout()
            _isLoading.value = false
        }
    }
}
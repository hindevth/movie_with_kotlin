package com.hin.movie.ui.edit_profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.hin.movie.data.entities.User
import com.hin.movie.data.repository.AuthRepository
import com.hin.movie.ui.base.BaseViewModel
import com.hin.movie.ui.profile.data.UIState
import com.hin.movie.utils.extensions.toUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseAuth: FirebaseAuth,
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        _user.value = firebaseAuth.currentUser?.toUser()
    }
}
package com.hin.movie.ui.history

import androidx.lifecycle.viewModelScope
import com.hin.movie.data.repository.HistoryRepository
import com.hin.movie.ui.base.BaseViewModel
import com.hin.movie.ui.history.data.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val historyRepository: HistoryRepository) :
    BaseViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    init {
        loadHistories()
    }

    fun loadHistories() {
        _isLoading.value = true
        viewModelScope.launch {
            val data = historyRepository.getHistories()
            _uiState.value = _uiState.value.copy(movies = data)
            _isLoading.value = false
        }
    }
}
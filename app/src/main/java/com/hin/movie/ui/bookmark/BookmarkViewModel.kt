package com.hin.movie.ui.bookmark

import androidx.lifecycle.viewModelScope
import com.hin.movie.data.repository.BookmarkRepository
import com.hin.movie.ui.base.BaseViewModel
import com.hin.movie.ui.bookmark.data.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(private val bookmarkRepository: BookmarkRepository): BaseViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    init {
        if (uiState.value.movies == null) {
            loadBookmark()
        }
    }

    fun loadBookmark(){
        _isLoading.value = true
        viewModelScope.launch {
            val data = bookmarkRepository.bookmarks(uiState.value.lastDocument)
            Timber.i(data.first.toString())
            _uiState.value = _uiState.value.copy(movies = data.first, lastDocument = data.second)
            _isLoading.value = false
        }
    }

    fun clearLastDocument(){
        _uiState.value = _uiState.value.copy(lastDocument = null)
    }
}
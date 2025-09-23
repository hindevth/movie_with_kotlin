package com.hin.flixcomix.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hin.flixcomix.data.entities.Movie
import com.hin.flixcomix.data.repository.MovieRepository
import com.hin.flixcomix.ui.base.BaseViewModel
import com.hin.flixcomix.ui.home.data.HomeUiState
import com.hin.flixcomix.utils.MovieType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val movieRepository: MovieRepository) :
    BaseViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState())

    val uiState: StateFlow<HomeUiState> = _uiState


    fun loadNewMovies() {
        if (uiState.value.newMovies != null && uiState.value.newMovies?.isNotEmpty() == true) return
        _isLoading.value = true
        try {
            viewModelScope.launch {
                val data = async { movieRepository.getNewMovie() }
                val dataSeries = async { movieRepository.getMovieByType(MovieType.PHIM_BO) }
                val dataSingle = async { movieRepository.getMovieByType(MovieType.PHIM_LE) }
                val dataTVShows = async { movieRepository.getMovieByType(MovieType.TV_SHOWS) }
                val dataCartoons = async { movieRepository.getMovieByType(MovieType.HOAT_HINH) }

                _uiState.update {
                    it.copy(
                        newMovies = data.await().items,
                        singleMovies = dataSingle.await().data?.items,
                        seriesMovies = dataSeries.await().data?.items,
                        tvShows = dataTVShows.await().data?.items,
                        cartoons = dataCartoons.await().data?.items,
                    )
                }
            }
        } catch (e: Exception) {
            Timber.e(e.toString())
        } finally {
            _isLoading.value = false
        }

    }
}
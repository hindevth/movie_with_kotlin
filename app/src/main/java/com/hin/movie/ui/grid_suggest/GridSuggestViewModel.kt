package com.hin.movie.ui.grid_suggest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hin.movie.data.entities.Movie
import com.hin.movie.data.repository.MovieRepository
import com.hin.movie.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class GridSuggestViewModel @Inject constructor(val repository: MovieRepository) : BaseViewModel() {
    private val _movies = MutableLiveData<List<Movie>?>()
    val movies: LiveData<List<Movie>?> = _movies

    fun loadMovies(slug: String, slugMovie: String?){
        if (_movies.value != null) return
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val data = repository.getMovieByGenre(slug)
                val mvs = data.data?.items.orEmpty()
                    .filter { it.slug != slugMovie }.dropLast(1)
                _movies.postValue(mvs)
            } catch (e: Exception) {
                Timber.e(e.toString())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
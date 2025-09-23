package com.hin.flixcomix.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hin.flixcomix.data.entities.Episode
import com.hin.flixcomix.data.entities.Movie
import com.hin.flixcomix.data.entities.Server
import com.hin.flixcomix.data.repository.MovieRepository
import com.hin.flixcomix.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class DetailViewModel @Inject constructor(private val movieRepository: MovieRepository) : BaseViewModel() {

    private val _movie = MutableLiveData<Movie?>()
    val movie: LiveData<Movie?> = _movie

    private val _servers = MutableLiveData<List<Server>?>()
    val servers: LiveData<List<Server>?> = _servers

    fun setMovie(movie: Movie?){
        if (_movie.value?.content != null) return
        _movie.value = movie
    }

    fun loadMovieDetail(slug: String?){

        if (slug.isNullOrEmpty() || _movie.value?.content != null) return
        _isLoading.value = true
        viewModelScope.launch {
            val data = movieRepository.getMovieDetail(slug)
            setMovie(data.movie)
            _servers.postValue(data.servers)
            _isLoading.value = false
        }
    }
}
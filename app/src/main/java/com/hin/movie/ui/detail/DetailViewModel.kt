package com.hin.movie.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hin.movie.data.entities.Movie
import com.hin.movie.data.entities.Server
import com.hin.movie.data.repository.MovieRepository
import com.hin.movie.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

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
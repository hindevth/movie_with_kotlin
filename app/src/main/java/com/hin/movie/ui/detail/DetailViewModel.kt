package com.hin.movie.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hin.movie.data.entities.Movie
import com.hin.movie.data.entities.Server
import com.hin.movie.data.repository.BookmarkRepository
import com.hin.movie.data.repository.MovieRepository
import com.hin.movie.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val bookmarkRepository: BookmarkRepository
) : BaseViewModel() {

    private val _movie = MutableLiveData<Movie?>()
    val movie: LiveData<Movie?> = _movie

    private val _servers = MutableLiveData<List<Server>?>()
    val servers: LiveData<List<Server>?> = _servers

    private val _isBookmark = MutableLiveData<Boolean>(false)
    val isBookmark: LiveData<Boolean> = _isBookmark

    fun setMovie(movie: Movie?) {
        if (_movie.value?.content != null) return
        _movie.value = movie
    }

    fun loadMovieDetail(slug: String?) {
        if (slug.isNullOrEmpty() || servers.value != null) return
        _isLoading.value = true
        viewModelScope.launch {
            _isBookmark.postValue(bookmarkRepository.bookmarkIsExits(movie.value!!))
            val data = movieRepository.getMovieDetail(slug)
            setMovie(data.movie)
            _servers.postValue(data.servers)
            _isLoading.value = false
        }
    }

    fun toggleBookmark() {
        _isLoading.value = true
        viewModelScope.launch {
            if (isBookmark.value == true){
                bookmarkRepository.removeBookmark(movie.value!!)
                _isBookmark.postValue(false)
                return@launch
            }
            bookmarkRepository.addBookmark(movie.value!!)
            _isBookmark.postValue(true)
            _isLoading.value = false
        }
    }
}
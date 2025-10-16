package com.hin.movie.ui.movie_more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hin.movie.data.entities.Movie
import com.hin.movie.data.entities.Pagination
import com.hin.movie.data.repository.MovieRepository
import com.hin.movie.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MovieMoreViewModel @Inject constructor(private val movieRepository: MovieRepository) : BaseViewModel() {
    private val _movies = MutableLiveData<List<Movie>?>()
    val movies: LiveData<List<Movie>?> = _movies

    private val _pagination = MutableLiveData<Pagination>(Pagination(currentPage = 1))
    val pagination: LiveData<Pagination> = _pagination

    private val _typeMovie = MutableLiveData<String>()
    val typeMovie: LiveData<String> = _typeMovie


    fun setTypeMovie(slug: String?){
        _typeMovie.value = slug ?: ""
    }
    fun setMovies(movies: List<Movie>?) {
        _movies.value = movies
    }

    fun nextPage() {
        if (pagination.value?.currentPage!! < pagination.value?.totalPages!!) {
            _pagination.value =
                pagination.value?.copy(currentPage = pagination.value?.currentPage!! + 1)
        }

        fetchMovie()
    }

    fun changeCurrentPage(page: Int) {
        _pagination.value = pagination.value?.copy(currentPage = page)
    }

    fun fetchMovie() {
        if (typeMovie.value == "new"){
            fetchMovieNew()
        }else{
            fetchMovieType()
        }
    }

    fun fetchMovieNew(){
        if (_isLoading.value == true) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val pagination = pagination.value ?: Pagination(currentPage = 1)

                val data = movieRepository.getNewMovie(
                    page = pagination.currentPage,
                    limit = 26,
                )
                if (pagination.currentPage!! > 1) {
                    _movies.value = movies.value.orEmpty() + data.items.orEmpty()
                } else {
                    _movies.value = data.items
                }
                val pagi = data.pagination
                _pagination.value = pagination.copy(
                    totalItems = pagi?.totalItems,
                    totalPages = pagi?.totalPages,
                    totalItemsPerPage = pagi?.totalItemsPerPage
                )
            } catch (e: Exception) {
                _movies.value = emptyList()
                Timber.e(e)
            } finally {
                delay(1000)
                _isLoading.value = false
            }
        }
    }

    fun fetchMovieType() {
        if (_isLoading.value == true) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val pagination = pagination.value ?: Pagination(currentPage = 1)

                val data = movieRepository.getMovieByType(
                    typeMovie.value ?: "",
                    page = pagination.currentPage,
                    limit = 26,
                )
                if (pagination.currentPage!! > 1) {
                    _movies.value = movies.value.orEmpty() + data.data?.items.orEmpty()
                } else {
                    _movies.value = data.data?.items
                }
                val pagi = data.data?.params?.pagination
                _pagination.value = pagination.copy(
                    totalItems = pagi?.totalItems,
                    totalPages = pagi?.totalPages,
                    totalItemsPerPage = pagi?.totalItemsPerPage
                )
            } catch (e: Exception) {
                _movies.value = emptyList()
                Timber.e(e)
            } finally {
                delay(1000)
                _isLoading.value = false
            }
        }
    }
}
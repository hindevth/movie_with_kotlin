package com.hin.movie.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hin.movie.data.entities.Country
import com.hin.movie.data.entities.Genre
import com.hin.movie.data.entities.Movie
import com.hin.movie.data.entities.Pagination
import com.hin.movie.data.entities.Sort
import com.hin.movie.data.repository.MovieRepository
import com.hin.movie.ui.base.BaseViewModel
import com.hin.movie.ui.search.model.FilterState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SearchViewModel @Inject constructor(private val movieRepository: MovieRepository) :
    BaseViewModel() {
    private val _filterState = MutableLiveData(FilterState())
    val filterData = MediatorLiveData<FilterState>()

    private val _movies = MutableLiveData<List<Movie>?>()
    val movies: LiveData<List<Movie>?> = _movies

    private val _pagination = MutableLiveData<Pagination>(Pagination(currentPage = 1))
    val pagination: LiveData<Pagination> = _pagination

    private var debounceJob: Job? = null

    init {
        filterData.addSource(_filterState) { state ->
            debounceJob?.cancel()
            debounceJob = viewModelScope.launch {
                val delayTime = if (state.search != filterData.value?.search) 500L else 0L
                delay(delayTime)
                filterData.value = state
                if (delayTime > 0) {
                    _pagination.value = Pagination(currentPage = 1)
                    fetchSearchAndFilter()
                }
            }
        }
    }

    fun nextPage() {
        if (pagination.value?.currentPage!! < pagination.value?.totalPages!!) {
            _pagination.value =
                pagination.value?.copy(currentPage = pagination.value?.currentPage!! + 1)
        }

        fetchSearchAndFilter()
    }

    fun changeCurrentPage(page: Int) {
        _pagination.value = pagination.value?.copy(currentPage = page)
    }

    fun changeSort(item: Sort) {
        val current = _filterState.value ?: FilterState()
        _filterState.value = if (current.sort?.slug == item.slug) {
            current.copy(sort = null)
        } else {
            current.copy(sort = item)
        }
    }

    fun setSearch(search: String) {
        val current = _filterState.value ?: FilterState()
        _filterState.value = current.copy(search)
    }

    fun changeCountry(item: Country) {
        val current = _filterState.value ?: FilterState()
        _filterState.value = if (current.country?.slug == item.slug) {
            current.copy(country = null)
        } else {
            current.copy(country = item)
        }
    }

    fun changeGenre(item: Genre) {
        val current = _filterState.value ?: FilterState()
        _filterState.value = if (current.genre?.slug == item.slug) {
            current.copy(genre = null)
        } else {
            current.copy(genre = item)
        }
    }

    fun resetFilter() {
        _filterState.value = FilterState(search = _filterState.value?.search)
    }

    fun fetchSearchAndFilter() {
        if (_isLoading.value == true) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val pagination = pagination.value ?: Pagination(currentPage = 1)
                val state = _filterState.value
                Timber.i("fetchSearchAndFilter ${state?.search}")

                val data = movieRepository.getMovieSearch(
                    state?.search ?: "",
                    page = pagination.currentPage,
                    sortType = state?.sort?.slug,
                    category = state?.genre?.slug,
                    country = state?.country?.slug,
                    year = state?.year
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
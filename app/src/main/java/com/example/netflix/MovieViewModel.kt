package com.example.netflix

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.netflix.retrofit.movieApi
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.netflix.UserPreferences.AuthPreferences
import kotlinx.coroutines.launch

class MovieViewModel(private val accessToken: String ): ViewModel() {
    private val _movies = mutableStateOf<List<Movie>>(emptyList())
    val movies: State<List<Movie>> get() = _movies

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> get() = _isLoading


    init {
        Log.d("MovieViewModel", "ViewModel initialized")
        fetchMovies()
    }


    private fun fetchMovies() {

        viewModelScope.launch {
            Log.d("MovieViewModel", "Starting fetchMovies()")
            _isLoading.value = true
            try {
                Log.d("MovieViewModel", "Started fetching with ${accessToken}")
                val response = movieApi.getMovies(accessToken)
                Log.d("MovieViewModel", "Movies fetched: ${response.size}")
                _movies.value = response
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error fetching movies: ${e.message}")
                e.printStackTrace()
            } finally {
                _isLoading.value = false
                Log.d("MovieViewModel", "Loading finished")
            }
        }
    }
}

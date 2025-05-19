package com.example.netflix

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netflix.retrofit.showApi
import kotlinx.coroutines.launch

class ShowViewModel: ViewModel() {
    private val _shows = mutableStateOf<List<Series>>(emptyList())
    val shows: State<List<Series>> get() = _shows

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> get() = _isLoading

    init {
        Log.d("ShowViewModel", "ViewModel initialized")
        fetchShows()
    }

    private fun fetchShows() {
        viewModelScope.launch {
            Log.d("ShowViewModel", "Starting fetchMovies()")
            _isLoading.value = true
            try {
                val response = showApi.getShows()
                Log.d("ShowViewModel", "Shows fetched: ${response.size}")
                _shows.value = response
            } catch (e: Exception) {
                Log.e("ShowViewModel", "Error fetching shows: ${e.message}")
                e.printStackTrace()
            } finally {
                _isLoading.value = false
                Log.d("ShowViewModel", "Loading finished")
            }
        }
    }
}
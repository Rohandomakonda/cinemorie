package com.example.netflix

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// ViewModel factory
class MovieViewModelFactory(private val accessToken: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(accessToken) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

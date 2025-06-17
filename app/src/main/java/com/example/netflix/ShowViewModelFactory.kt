package com.example.netflix

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShowViewModelFactory(private val accessToken: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ShowViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShowViewModel(accessToken) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
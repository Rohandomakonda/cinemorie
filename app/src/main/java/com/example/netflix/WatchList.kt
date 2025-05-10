package com.example.netflix

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun WatchList(navController: NavController) {
    val items = listOf("Apple", "Banana", "Cherry")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items()
    }


}
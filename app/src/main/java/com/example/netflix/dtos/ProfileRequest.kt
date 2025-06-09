package com.example.netflix.dtos

data class ProfileRequest(
    val userid: Long,
    val name: String,
    val selectedImage: Long
)
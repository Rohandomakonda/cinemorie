package com.example.netflix.dtos

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

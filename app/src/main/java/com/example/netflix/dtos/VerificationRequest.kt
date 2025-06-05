package com.example.netflix.dtos

data class VerificationRequest(
    val email: String,
    val otp: String
)

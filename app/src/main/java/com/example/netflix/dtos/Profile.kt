package com.example.netflix.dtos

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: Long,
    val userId: Long,
    val name: String,
    var avatar: Long,
)
package com.example.open5e.network

data class ApiResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)
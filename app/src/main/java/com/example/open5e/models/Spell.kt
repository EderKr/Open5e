package com.example.open5e.models

data class Spell(
    val name: String,
    val slug: String,
    val level: String,
    val school: String?,
    val description: String?,        // desc
    val higher_level: String? = null,
    val casting_time: String?,
    val range: String?,
    val components: String?,
    val duration: String?
)
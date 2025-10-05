package com.example.open5e.models

data class Spell(
    val name: String,
    val level: String,
    val description: String,
    val casting_time: String,
    val range: String,
    val components: String,
    val duration: String
) {
    val school: Any = TODO()
}

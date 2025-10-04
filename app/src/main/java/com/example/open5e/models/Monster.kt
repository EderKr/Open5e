package com.example.open5e.models

data class Monster(
    val name: String,
    val type: String,
    val challenge_rating: Double,
    val hit_points: Int,
    val armor_class: Int,
    val abilities: Map<String, Int>
) {
    val hitPoints: String
        get() = "$hit_points HP"
}

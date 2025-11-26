package com.example.open5e.models

data class Monster(
    val name: String,
    val slug: String,
    val type: String?,
    val challenge_rating: String,
    val hit_points: Int,
    val armor_class: Int,
    val strength: Int? = null,
    val dexterity: Int? = null,
    val constitution: Int? = null,
    val intelligence: Int? = null,
    val wisdom: Int? = null,
    val charisma: Int? = null,
    val desc: String? = null
) {
    val hitPoints: String
        get() = "$hit_points HP"
}
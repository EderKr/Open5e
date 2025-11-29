package com.example.open5e.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.open5e.models.Monster

@Entity(tableName = "monsters")
data class MonsterEntity(
    @PrimaryKey val slug: String,
    val name: String,
    val challenge_rating: String?,
    val hit_points: Int?,
    val armor_class: Int?,
    val timestamp: Long = System.currentTimeMillis(),
    val json: String
)

fun Monster.toEntity(json: String): MonsterEntity {
    return MonsterEntity(
        slug = this.slug ?: throw IllegalArgumentException("Monster.slug is null"),
        name = this.name ?: "Unknown",
        challenge_rating = this.challenge_rating,
        hit_points = this.hit_points,
        armor_class = this.armor_class,
        json = json
    )
}
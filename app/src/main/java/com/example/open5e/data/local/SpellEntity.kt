package com.example.open5e.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.open5e.models.Spell

@Entity(tableName = "spells")
data class SpellEntity(
    @PrimaryKey val slug: String,
    val name: String,
    val level: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val json: String
)

fun Spell.toEntity(json: String): SpellEntity {
    return SpellEntity(
        slug = this.slug,
        name = this.name,
        level = this.level,
        json = json
    )
}
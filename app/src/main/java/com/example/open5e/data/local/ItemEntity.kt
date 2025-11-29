package com.example.open5e.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.open5e.models.MagicItem

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val slug: String,
    val name: String,
    val rarity: String?,
    val type: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val json: String
)

fun MagicItem.toEntity(json: String): ItemEntity {
    return ItemEntity(
        slug = this.slug,
        name = this.name,
        rarity = this.rarity,
        type = this.type,
        json = json
    )
}
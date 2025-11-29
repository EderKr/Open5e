package com.example.open5e.data

import com.example.open5e.data.local.ItemEntity
import com.example.open5e.data.local.MonsterEntity
import com.example.open5e.data.local.SpellEntity
import com.example.open5e.models.MagicItem
import com.example.open5e.models.Monster
import com.example.open5e.models.Spell

// -------------------------
// MONSTER
// -------------------------

fun Monster.toEntity(json: String) = MonsterEntity(
    slug = this.slug ?: "",
    name = this.name ?: "Unknown",
    challenge_rating = this.challenge_rating,
    hit_points = this.hit_points,
    armor_class = this.armor_class,
    json = json
)

fun MonsterEntity.toModel(): Monster {
    return com.google.gson.Gson().fromJson(this.json, Monster::class.java)
}


// -------------------------
// SPELL
// -------------------------

fun Spell.toEntity(json: String) = SpellEntity(
    slug = this.slug,
    name = this.name,
    level = this.level,
    json = json
)

fun SpellEntity.toModel(): Spell {
    return com.google.gson.Gson().fromJson(this.json, Spell::class.java)
}


// -------------------------
// MAGIC ITEM
// -------------------------

fun MagicItem.toEntity(json: String) = ItemEntity(
    slug = this.slug,
    name = this.name,
    rarity = this.rarity,
    type = this.type,
    json = json
)

fun ItemEntity.toModel(): MagicItem {
    return com.google.gson.Gson().fromJson(this.json, MagicItem::class.java)
}
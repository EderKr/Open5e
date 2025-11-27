package com.example.open5e.models

data class MagicItem(
    val name: String,
    val slug: String,
    val rarity: String,
    val type: String,
    val desc: String?,
    val value: String? = null
)
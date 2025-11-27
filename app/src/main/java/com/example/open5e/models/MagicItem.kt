package com.example.open5e.models

import com.google.gson.annotations.SerializedName

data class MagicItem(
    val name: String,
    val slug: String,
    val rarity: String,
    val type: String,

    @SerializedName("desc")
    val description: String?,

    val value: String? = null
)
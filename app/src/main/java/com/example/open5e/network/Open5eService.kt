package com.example.open5e.network

import com.example.open5e.models.MagicItem
import com.example.open5e.models.Monster
import com.example.open5e.models.Spell
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Open5eService {

    @GET("monsters/")
    suspend fun getMonsters(
        @Query("page") page: Int = 1
    ): Response<ApiResponse<Monster>>

    @GET("spells/")
    suspend fun getSpells(
        @Query("page") page: Int = 1
    ): Response<ApiResponse<Spell>>

    @GET("magicitems/")
    suspend fun getItems(
        @Query("rarity") rarity: String = "",
        @Query("type") type: String = "",
        @Query("page") page: Int = 1
    ): Response<ApiResponse<MagicItem>>
}
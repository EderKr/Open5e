package com.example.open5e.network

import com.example.open5e.models.MagicItem
import com.example.open5e.models.Monster
import com.example.open5e.models.Spell
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Open5eService {

    @GET("monsters/")
    suspend fun getMonsters(
        @Query("page") page: Int = 1,
        @Query("cr") cr: String? = null
    ): Response<ApiResponse<Monster>>

    @GET("spells/")
    suspend fun getSpells(
        @Query("page") page: Int = 1,
        @Query("level") level: String? = null
    ): Response<ApiResponse<Spell>>

    @GET("magicitems/")
    suspend fun getItems(
        @Query("rarity") rarity: String? = null,
        @Query("type") type: String? = null,
        @Query("page") page: Int = 1
    ): Response<ApiResponse<MagicItem>>

    @GET("monsters/{slug}/")
    suspend fun getMonsterDetail(@Path("slug") slug: String): Response<Monster>

    @GET("spells/{slug}/")
    suspend fun getSpellDetail(@Path("slug") slug: String): Response<Spell>

    @GET("magicitems/{slug}/")
    suspend fun getItemDetail(@Path("slug") slug: String): Response<MagicItem>
}
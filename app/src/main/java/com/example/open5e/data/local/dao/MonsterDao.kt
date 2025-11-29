package com.example.open5e.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.open5e.data.local.MonsterEntity

@Dao
interface MonsterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(monster: MonsterEntity)

    @Query("DELETE FROM monsters WHERE slug = :slug")
    suspend fun delete(slug: String)

    @Query("SELECT * FROM monsters WHERE slug = :slug LIMIT 1")
    suspend fun getDetail(slug: String): MonsterEntity?

    @Query("SELECT * FROM monsters ORDER BY name ASC")
    suspend fun getAll(): List<MonsterEntity>

    @Query("SELECT * FROM monsters WHERE challenge_rating = :cr ORDER BY name ASC")
    suspend fun getByCR(cr: String): List<MonsterEntity>
}

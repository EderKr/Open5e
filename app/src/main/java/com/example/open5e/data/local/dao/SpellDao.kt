package com.example.open5e.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.open5e.data.local.SpellEntity

@Dao
interface SpellDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(spell: SpellEntity)

    @Query("DELETE FROM spells WHERE slug = :slug")
    suspend fun delete(slug: String)

    @Query("SELECT * FROM spells WHERE slug = :slug LIMIT 1")
    suspend fun getDetail(slug: String): SpellEntity?

    @Query("SELECT * FROM spells ORDER BY name ASC")
    suspend fun getAll(): List<SpellEntity>

    @Query("SELECT * FROM spells WHERE level = :level ORDER BY name ASC")
    suspend fun getByLevel(level: String): List<SpellEntity>
}

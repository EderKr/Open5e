package com.example.open5e.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.open5e.data.local.ItemEntity

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ItemEntity)

    @Query("DELETE FROM items WHERE slug = :slug")
    suspend fun delete(slug: String)

    @Query("SELECT * FROM items WHERE slug = :slug LIMIT 1")
    suspend fun getDetail(slug: String): ItemEntity?

    @Query("SELECT * FROM items ORDER BY name ASC")
    suspend fun getAll(): List<ItemEntity>

    @Query("""
        SELECT * FROM items 
        WHERE (:rarity = '' OR rarity = :rarity)
          AND (:type = '' OR type = :type)
        ORDER BY name ASC
    """)
    suspend fun getFiltered(rarity: String, type: String): List<ItemEntity>
}
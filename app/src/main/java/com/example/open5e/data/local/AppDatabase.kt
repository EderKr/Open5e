package com.example.open5e.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.open5e.data.local.dao.ItemDao
import com.example.open5e.data.local.dao.MonsterDao
import com.example.open5e.data.local.dao.SpellDao

@Database(
    entities = [
        MonsterEntity::class,
        SpellEntity::class,
        ItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun monsterDao(): MonsterDao
    abstract fun spellDao(): SpellDao
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "open5e.db"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
package com.example.open5e.data

import android.content.Context
import com.example.open5e.data.local.AppDatabase
import com.example.open5e.data.local.toEntity
import com.example.open5e.models.MagicItem
import com.example.open5e.models.Monster
import com.example.open5e.models.Spell
import com.example.open5e.network.ApiClient
import com.example.open5e.network.Open5eService
import com.google.gson.Gson

class Open5eRepository(context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val monsterDao = db.monsterDao()
    private val spellDao = db.spellDao()
    private val itemDao = db.itemDao()
    private val api = ApiClient.createService(Open5eService::class.java)
    private val gson = Gson()

    // ---------------------------------------------------------
    // DETAIL (ONLINE + OFFLINE)
    // ---------------------------------------------------------

    suspend fun fetchMonsterOnline(slug: String): Monster? =
        api.getMonsterDetail(slug).body()

    suspend fun fetchSpellOnline(slug: String): Spell? =
        api.getSpellDetail(slug).body()

    suspend fun fetchItemOnline(slug: String): MagicItem? =
        api.getItemDetail(slug).body()

    suspend fun getMonsterOffline(slug: String): Monster? =
        monsterDao.getDetail(slug)?.let { gson.fromJson(it.json, Monster::class.java) }

    suspend fun getSpellOffline(slug: String): Spell? =
        spellDao.getDetail(slug)?.let { gson.fromJson(it.json, Spell::class.java) }

    suspend fun getItemOffline(slug: String): MagicItem? =
        itemDao.getDetail(slug)?.let { gson.fromJson(it.json, MagicItem::class.java) }

    // ---------------------------------------------------------
    // SAVE
    // ---------------------------------------------------------

    suspend fun saveMonster(m: Monster) =
        monsterDao.insert(m.toEntity(gson.toJson(m)))

    suspend fun saveSpell(s: Spell) =
        spellDao.insert(s.toEntity(gson.toJson(s)))

    suspend fun saveItem(i: MagicItem) =
        itemDao.insert(i.toEntity(gson.toJson(i)))

    // ---------------------------------------------------------
    // PAGINATION ONLINE (SEM FILTROS)
    // ---------------------------------------------------------

    suspend fun fetchMonstersPageOnline(page: Int): List<Monster>? {
        val response = api.getMonsters(page = page)
        if (!response.isSuccessful) return null
        val body = response.body() ?: return null

        body.results.forEach { monsterDao.insert(it.toEntity(gson.toJson(it))) }

        return body.results
    }

    suspend fun fetchSpellsPageOnline(page: Int, string: String?): List<Spell>? {
        val response = api.getSpells(page = page)
        if (!response.isSuccessful) return null
        val body = response.body() ?: return null

        body.results.forEach { spellDao.insert(it.toEntity(gson.toJson(it))) }

        return body.results
    }

    suspend fun fetchItemsPageOnline(page: Int, rarity: String, type: String): List<MagicItem>? {
        val response = api.getItems(
            rarity = rarity,
            type = type,
            page = page
        )
        if (!response.isSuccessful) return null
        val body = response.body() ?: return null

        body.results.forEach { itemDao.insert(it.toEntity(gson.toJson(it))) }

        return body.results
    }

    // ---------------------------------------------------------
    // OFFLINE LISTING
    // ---------------------------------------------------------

    suspend fun getAllMonstersOffline(): List<Monster> =
        monsterDao.getAll().map { gson.fromJson(it.json, Monster::class.java) }

    suspend fun getAllSpellsOffline(): List<Spell> =
        spellDao.getAll().map { gson.fromJson(it.json, Spell::class.java) }

    suspend fun getItemsOffline(rarity: String, type: String): List<MagicItem> =
        itemDao.getFiltered(rarity, type).map { gson.fromJson(it.json, MagicItem::class.java) }

    suspend fun getMonstersByCROffline(cr: String): List<Monster> =
        monsterDao.getByCR(cr).map { gson.fromJson(it.json, Monster::class.java) }

    suspend fun getSpellsByLevelOffline(level: String): List<Spell> =
        spellDao.getByLevel(level).map { gson.fromJson(it.json, Spell::class.java) }
}
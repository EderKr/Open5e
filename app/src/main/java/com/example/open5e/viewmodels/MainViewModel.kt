package com.example.open5e.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.open5e.models.MagicItem
import com.example.open5e.models.Monster
import com.example.open5e.models.Spell
import com.example.open5e.network.ApiClient
import com.example.open5e.network.Open5eService
import kotlinx.coroutines.launch

private const val PAGE_SIZE = 15

class MainViewModel : ViewModel() {
    private val apiService: Open5eService = ApiClient.createService(Open5eService::class.java)
    private val allMonsters = mutableListOf<Monster>()
    private var monstersNextApiPage = 1
    private var monstersMoreAvailable = true
    private val allSpells = mutableListOf<Spell>()
    private var spellsNextApiPage = 1
    private var spellsMoreAvailable = true
    private val allItems = mutableListOf<MagicItem>()
    private var itemsNextApiPage = 1
    private var itemsMoreAvailable = true
    private var lastItemsRarity = ""
    private var lastItemsType = ""

    fun fetchMonstersPage(
        challengeRating: String,
        page: Int,
        onResult: (List<Monster>, Boolean, Boolean, Int, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val desiredStart = (page - 1) * PAGE_SIZE
                var filtered: List<Monster> = if (challengeRating.isBlank()) allMonsters.toList()
                else allMonsters.filter { it.challenge_rating.equals(challengeRating, ignoreCase = true) }
                while (filtered.size <= desiredStart && monstersMoreAvailable) {
                    val response = apiService.getMonsters(monstersNextApiPage)
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            allMonsters.addAll(body.results)
                            val unique = allMonsters.distinctBy { it.name }
                            allMonsters.clear()
                            allMonsters.addAll(unique)

                            monstersNextApiPage++
                            monstersMoreAvailable = body.next != null

                            filtered = if (challengeRating.isBlank()) allMonsters.toList()
                            else allMonsters.filter { it.challenge_rating.equals(challengeRating, ignoreCase = true) }
                        } else {
                            monstersMoreAvailable = false
                        }
                    } else {
                        monstersMoreAvailable = false
                    }
                }

                val totalPages = if (filtered.isEmpty() && !monstersMoreAvailable) 0
                else (filtered.size + PAGE_SIZE - 1) / PAGE_SIZE

                val pageItems = if (filtered.size > desiredStart) filtered.drop(desiredStart).take(PAGE_SIZE)
                else {
                    if (totalPages > 0) {
                        val lastStart = (totalPages - 1) * PAGE_SIZE
                        filtered.drop(lastStart).take(PAGE_SIZE)
                    } else {
                        emptyList()
                    }
                }

                val canBack = page > 1 && filtered.isNotEmpty()
                val canNext = (filtered.size > page * PAGE_SIZE) || monstersMoreAvailable

                onResult(pageItems, canBack, canNext, totalPages, null)
            } catch (t: Throwable) {
                onResult(emptyList(), false, false, 0, t.message)
            }
        }
    }

    fun fetchSpellsPage(
        level: String,
        page: Int,
        onResult: (List<Spell>, Boolean, Boolean, Int, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val desiredStart = (page - 1) * PAGE_SIZE

                var filtered: List<Spell> = if (level.isBlank()) allSpells.toList()
                else allSpells.filter { it.level.equals(level, ignoreCase = true) }

                while (filtered.size <= desiredStart && spellsMoreAvailable) {
                    val response = apiService.getSpells(spellsNextApiPage)
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            allSpells.addAll(body.results)
                            val unique = allSpells.distinctBy { it.name }
                            allSpells.clear()
                            allSpells.addAll(unique)

                            spellsNextApiPage++
                            spellsMoreAvailable = body.next != null

                            filtered = if (level.isBlank()) allSpells.toList()
                            else allSpells.filter { it.level.equals(level, ignoreCase = true) }
                        } else {
                            spellsMoreAvailable = false
                        }
                    } else {
                        spellsMoreAvailable = false
                    }
                }

                val totalPages = if (filtered.isEmpty() && !spellsMoreAvailable) 0
                else (filtered.size + PAGE_SIZE - 1) / PAGE_SIZE

                val pageItems = if (filtered.size > desiredStart) filtered.drop(desiredStart).take(PAGE_SIZE)
                else {
                    if (totalPages > 0) {
                        val lastStart = (totalPages - 1) * PAGE_SIZE
                        filtered.drop(lastStart).take(PAGE_SIZE)
                    } else emptyList()
                }

                val canBack = page > 1 && filtered.isNotEmpty()
                val canNext = (filtered.size > page * PAGE_SIZE) || spellsMoreAvailable

                onResult(pageItems, canBack, canNext, totalPages, null)
            } catch (t: Throwable) {
                onResult(emptyList(), false, false, 0, t.message)
            }
        }
    }

    fun fetchItemsPage(
        rarity: String,
        type: String,
        page: Int,
        onResult: (List<MagicItem>, Boolean, Boolean, Int, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (rarity != lastItemsRarity || type != lastItemsType) {
                    allItems.clear()
                    itemsNextApiPage = 1
                    itemsMoreAvailable = true
                    lastItemsRarity = rarity
                    lastItemsType = type
                }

                val desiredStart = (page - 1) * PAGE_SIZE

                var filtered: List<MagicItem> = if (rarity.isBlank() && type.isBlank()) allItems.toList()
                else allItems.filter {
                    (rarity.isBlank() || it.rarity.equals(rarity, ignoreCase = true)) &&
                            (type.isBlank() || it.type.equals(type, ignoreCase = true))
                }

                while (filtered.size <= desiredStart && itemsMoreAvailable) {
                    val response = apiService.getItems(rarity, type, itemsNextApiPage)
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            allItems.addAll(body.results)
                            val unique = allItems.distinctBy { it.name }
                            allItems.clear()
                            allItems.addAll(unique)

                            itemsNextApiPage++
                            itemsMoreAvailable = body.next != null

                            filtered = if (rarity.isBlank() && type.isBlank()) allItems.toList()
                            else allItems.filter {
                                (rarity.isBlank() || it.rarity.equals(rarity, ignoreCase = true)) &&
                                        (type.isBlank() || it.type.equals(type, ignoreCase = true))
                            }
                        } else {
                            itemsMoreAvailable = false
                        }
                    } else {
                        itemsMoreAvailable = false
                    }
                }

                val totalPages = if (filtered.isEmpty() && !itemsMoreAvailable) 0
                else (filtered.size + PAGE_SIZE - 1) / PAGE_SIZE

                val pageItems = if (filtered.size > desiredStart) filtered.drop(desiredStart).take(PAGE_SIZE)
                else {
                    if (totalPages > 0) {
                        val lastStart = (totalPages - 1) * PAGE_SIZE
                        filtered.drop(lastStart).take(PAGE_SIZE)
                    } else emptyList()
                }

                val canBack = page > 1 && filtered.isNotEmpty()
                val canNext = (filtered.size > page * PAGE_SIZE) || itemsMoreAvailable

                onResult(pageItems, canBack, canNext, totalPages, null)
            } catch (t: Throwable) {
                onResult(emptyList(), false, false, 0, t.message)
            }
        }
    }

    fun fetchMonsterDetail(slug: String, onResult: (Monster?, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.getMonsterDetail(slug)
                if (response.isSuccessful) onResult(response.body(), null)
                else onResult(null, "Failed: ${response.message()}")
            } catch (t: Throwable) {
                onResult(null, t.message)
            }
        }
    }

    fun fetchSpellDetail(slug: String, onResult: (Spell?, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.getSpellDetail(slug)
                if (response.isSuccessful) onResult(response.body(), null)
                else onResult(null, "Failed: ${response.message()}")
            } catch (t: Throwable) {
                onResult(null, t.message)
            }
        }
    }

    fun fetchItemDetail(slug: String, onResult: (MagicItem?, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.getItemDetail(slug)
                if (response.isSuccessful) onResult(response.body(), null)
                else onResult(null, "Failed: ${response.message()}")
            } catch (t: Throwable) {
                onResult(null, t.message)
            }
        }
    }
}

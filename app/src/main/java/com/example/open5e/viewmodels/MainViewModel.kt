package com.example.open5e.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.open5e.data.Open5eRepository
import com.example.open5e.models.MagicItem
import com.example.open5e.models.Monster
import com.example.open5e.models.Spell
import kotlinx.coroutines.launch

private const val UI_PAGE = 15

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = Open5eRepository(application)

    private val monstersBuffer = mutableListOf<Monster>()
    private val spellsBuffer = mutableListOf<Spell>()
    private val itemsBuffer = mutableListOf<MagicItem>()

    private var nextMonPage = 1
    private var nextSpellPage = 1
    private var nextItemPage = 1

    private var monHasMore = true
    private var spellHasMore = true
    private var itemHasMore = true

    private var lastCR = ""
    private var lastLevel = ""
    private var lastRarity = ""
    private var lastType = ""

    private fun normalize(s: String?): String {
        if (s.isNullOrBlank()) return ""
        return s.lowercase().replace("\\s+".toRegex(), "")
            .replace("-level", "")
            .replace("th", "")
            .replace("st", "")
            .replace("nd", "")
            .replace("rd", "")
            .replace("º", "")
            .replace(".", "")
            .trim()
    }

    private fun <T> page(items: List<T>, page: Int): List<T> {
        val p = if (page < 1) 1 else page
        val start = (p - 1) * UI_PAGE
        if (start >= items.size) return emptyList()
        return items.drop(start).take(UI_PAGE)
    }

    private fun <T> totalPages(list: List<T>) =
        if (list.isEmpty()) 0 else (list.size + UI_PAGE - 1) / UI_PAGE

    fun fetchMonstersPage(
        cr: String,
        page: Int,
        cb: (List<Monster>, Boolean, Boolean, Int, String?) -> Unit
    ) {
        viewModelScope.launch {
            if (cr != lastCR) {
                monstersBuffer.clear()
                nextMonPage = 1
                monHasMore = true
                lastCR = cr
            }

            var filtered: List<Monster>
            do {
                filtered = if (cr.isBlank()) monstersBuffer
                else monstersBuffer.filter { it.challenge_rating == cr }

                if (filtered.size >= page * UI_PAGE) break
                if (!monHasMore) break

                val api = try { repo.fetchMonstersPageOnline(nextMonPage) } catch (_: Throwable) { null }
                if (api.isNullOrEmpty()) {
                    monHasMore = false
                    break
                }
                monstersBuffer.addAll(api)
                nextMonPage++
            } while (true)

            filtered = if (cr.isBlank()) monstersBuffer else monstersBuffer.filter { it.challenge_rating == cr }
            if (filtered.isEmpty()) {
                val off = if (cr.isBlank()) repo.getAllMonstersOffline() else repo.getMonstersByCROffline(cr)
                val total = totalPages(off)
                cb(page(off, page), page > 1, page < total, total, null)
                return@launch
            }
            val total = totalPages(filtered)
            cb(page(filtered, page), page > 1, page < total, total, null)
        }
    }

    fun fetchSpellsPage(
        level: String,
        page: Int,
        cb: (List<Spell>, Boolean, Boolean, Int, String?) -> Unit
    ) {
        viewModelScope.launch {
            if (level != lastLevel) {
                spellsBuffer.clear()
                nextSpellPage = 1
                spellHasMore = true
                lastLevel = level
            }

            val normalized = normalize(level)
            fun matches(s: Spell): Boolean {
                val l = normalize(s.level)
                return l == normalized || l.contains(normalized) || normalized.contains(l)
            }

            var filtered: List<Spell>
            do {
                filtered = if (normalized.isBlank()) spellsBuffer else spellsBuffer.filter(::matches)
                if (filtered.size >= page * UI_PAGE) break
                if (!spellHasMore) break

                val api = try {
                    repo.fetchSpellsPageOnline(nextSpellPage, if (normalized.isBlank()) null else level)
                } catch (_: Throwable) { null }

                if (api.isNullOrEmpty()) {
                    spellHasMore = false
                    break
                }
                spellsBuffer.addAll(api)
                nextSpellPage++
            } while (true)

            filtered = if (normalized.isBlank()) spellsBuffer else spellsBuffer.filter(::matches)
            if (filtered.isEmpty()) {
                val off = if (normalized.isBlank()) repo.getAllSpellsOffline() else repo.getSpellsByLevelOffline(level)
                val total = totalPages(off)
                cb(page(off, page), page > 1, page < total, total, null)
                return@launch
            }
            val total = totalPages(filtered)
            cb(page(filtered, page), page > 1, page < total, total, null)
        }
    }

    fun fetchItemsPage(
        rarity: String,
        type: String,
        page: Int,
        cb: (List<MagicItem>, Boolean, Boolean, Int, String?) -> Unit
    ) {
        viewModelScope.launch {
            if (rarity != lastRarity || type != lastType) {
                itemsBuffer.clear()
                nextItemPage = 1
                itemHasMore = true
                lastRarity = rarity
                lastType = type
            }

            var filtered: List<MagicItem>
            do {
                filtered = itemsBuffer.filter {
                    (rarity.isBlank() || it.rarity.equals(rarity, true)) &&
                            (type.isBlank() || it.type.equals(type, true))
                }
                if (filtered.size >= page * UI_PAGE) break
                if (!itemHasMore) break

                val api = try { repo.fetchItemsPageOnline(nextItemPage, rarity, type) } catch (_: Throwable) { null }
                if (api.isNullOrEmpty()) {
                    itemHasMore = false
                    break
                }
                itemsBuffer.addAll(api)
                nextItemPage++
            } while (true)

            filtered = itemsBuffer.filter {
                (rarity.isBlank() || it.rarity.equals(rarity, true)) &&
                        (type.isBlank() || it.type.equals(type, true))
            }
            if (filtered.isEmpty()) {
                val off = repo.getItemsOffline(rarity, type)
                val total = totalPages(off)
                cb(page(off, page), page > 1, page < total, total, null)
                return@launch
            }
            val total = totalPages(filtered)
            cb(page(filtered, page), page > 1, page < total, total, null)
        }
    }

    fun saveMonsterOffline(m: Monster) = viewModelScope.launch { repo.saveMonster(m) }
    fun saveSpellOffline(s: Spell) = viewModelScope.launch { repo.saveSpell(s) }
    fun saveItemOffline(i: MagicItem) = viewModelScope.launch { repo.saveItem(i) }

    fun fetchMonsterDetail(slug: String, cb: (Monster?, String?) -> Unit) {
        viewModelScope.launch {
            val online = try { repo.fetchMonsterOnline(slug) } catch (_: Throwable) { null }
            if (online != null) { repo.saveMonster(online); cb(online, null); return@launch }
            val off = repo.getMonsterOffline(slug)
            if (off != null) cb(off, null) else cb(null, "Monster not available offline.")
        }
    }

    fun fetchSpellDetail(slug: String, cb: (Spell?, String?) -> Unit) {
        viewModelScope.launch {
            val online = try { repo.fetchSpellOnline(slug) } catch (_: Throwable) { null }
            if (online != null) { repo.saveSpell(online); cb(online, null); return@launch }
            val off = repo.getSpellOffline(slug)
            if (off != null) cb(off, null) else cb(null, "Spell not available offline.")
        }
    }

    fun fetchItemDetail(slug: String, cb: (MagicItem?, String?) -> Unit) {
        viewModelScope.launch {
            val online = try { repo.fetchItemOnline(slug) } catch (_: Throwable) { null }
            if (online != null) { repo.saveItem(online); cb(online, null); return@launch }
            val off = repo.getItemOffline(slug)
            if (off != null) cb(off, null) else cb(null, "Item not available offline.")
        }
    }
}
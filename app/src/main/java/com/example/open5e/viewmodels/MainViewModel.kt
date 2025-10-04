package com.example.open5e

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.open5e.models.Monster
import com.example.open5e.models.Spell
import com.example.open5e.models.MagicItem
import com.example.open5e.network.ApiClient
import com.example.open5e.network.Open5eService
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val apiService: Open5eService = ApiClient.createService(Open5eService::class.java)

    fun fetchMonsters(challengeRating: Double, onResult: (List<Monster>, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response: Response<List<Monster>> = apiService.getMonsters(challengeRating)
                if (response.isSuccessful) {
                    onResult(response.body() ?: emptyList(), null)
                } else {
                    onResult(emptyList(), "Failed: ${response.message()}")
                }
            } catch (e: Exception) {
                onResult(emptyList(), e.message)
            }
        }
    }

    fun fetchSpells(level: Int, onResult: (List<Spell>, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response: Response<List<Spell>> = apiService.getSpells(level)
                if (response.isSuccessful) {
                    onResult(response.body() ?: emptyList(), null)
                } else {
                    onResult(emptyList(), "Failed: ${response.message()}")
                }
            } catch (e: Exception) {
                onResult(emptyList(), e.message)
            }
        }
    }

    fun fetchItems(rarity: String, type: String, onResult: (List<MagicItem>, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response: Response<List<MagicItem>> = apiService.getItems(rarity, type)
                if (response.isSuccessful) {
                    onResult(response.body() ?: emptyList(), null)
                } else {
                    onResult(emptyList(), "Failed: ${response.message()}")
                }
            } catch (e: Exception) {
                onResult(emptyList(), e.message)
            }
        }
    }
}

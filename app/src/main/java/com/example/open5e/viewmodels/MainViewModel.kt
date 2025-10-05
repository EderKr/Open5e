package com.example.open5e.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.open5e.models.MagicItem
import com.example.open5e.models.Monster
import com.example.open5e.models.Spell
import com.example.open5e.network.ApiClient
import com.example.open5e.network.Open5eService
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val apiService: Open5eService = ApiClient.createService(Open5eService::class.java)

    fun fetchMonsters(challengeRating: String, onResult: (List<Monster>, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.getMonsters(challengeRating)
                if (response.isSuccessful) {
                    onResult(response.body()?.results ?: emptyList(), null)
                }
                else {
                    onResult(emptyList(), "Failed: ${response.message()}")
                }
            } catch (e: Exception) {
                onResult(emptyList(), e.message)
            }
        }
    }

    fun fetchSpells(level: String, onResult: (List<Spell>, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.getSpells(level)
                if (response.isSuccessful) {
                    onResult(response.body()?.results ?: emptyList(), null)
                }
                else {
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
                val response = apiService.getItems(rarity,type)
                if (response.isSuccessful) {
                    onResult(response.body()?.results ?: emptyList(), null)
                }
                else {
                    onResult(emptyList(), "Failed: ${response.message()}")
                }
            } catch (e: Exception) {
                onResult(emptyList(), e.message)
            }
        }
    }
}

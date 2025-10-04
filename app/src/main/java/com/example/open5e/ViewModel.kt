package com.example.open5e

/* class MainViewModel : ViewModel() {
    private val apiService: Open5eService = ApiClient.createService(Open5eService::class.java)

    fun fetchMonsters(challengeRating: Double, onResult: (List<Monster>, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response: Response<List<Monster>> = apiService.getMonsters(challengeRating)
                if (response.isSuccessful) {
                    // Successfully fetched monsters
                    onResult(response.body() ?: emptyList(), null)
                } else {
                    // Handle unsuccessful response
                    val errorMessage = "Failed to fetch monsters: HTTP ${response.code()} - ${response.message()}"
                    onResult(emptyList(), errorMessage)
                }
            } catch (e: Exception) {
                // Handle exceptions
                val errorMessage = "Error fetching monsters: ${e.localizedMessage}"
                e.printStackTrace()
                onResult(emptyList(), errorMessage)
            }
        }
    }

    fun fetchSpells(level: Int, onResult: (List<Spell>, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response: Response<List<Spell>> = apiService.getSpells(level)
                if (response.isSuccessful) {
                    // Successfully fetched spells
                    onResult(response.body() ?: emptyList(), null)
                } else {
                    // Handle unsuccessful response
                    val errorMessage = "Failed to fetch spells: HTTP ${response.code()} - ${response.message()}"
                    onResult(emptyList(), errorMessage)
                }
            } catch (e: Exception) {
                // Handle exceptions
                val errorMessage = "Error fetching spells: ${e.localizedMessage}"
                e.printStackTrace()
                onResult(emptyList(), errorMessage)
            }
        }
    }

    fun fetchItems(rarity: String, type: String, onResult: (List<MagicItem>, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response: Response<List<MagicItem>> = apiService.getItems(rarity, type)
                if (response.isSuccessful) {
                    // Successfully fetched magic items
                    onResult(response.body() ?: emptyList(), null)
                } else {
                    // Handle unsuccessful response
                    val errorMessage = "Failed to fetch items: HTTP ${response.code()} - ${response.message()}"
                    onResult(emptyList(), errorMessage)
                }
            } catch (e: Exception) {
                // Handle exceptions
                val errorMessage = "Error fetching items: ${e.localizedMessage}"
                e.printStackTrace()
                onResult(emptyList(), errorMessage)
            }
        }
    }
}
*/
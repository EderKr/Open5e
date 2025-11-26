package com.example.open5e.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.open5e.MainViewModel
import com.example.open5e.models.Monster

@Composable
fun MonsterDetailScreen(
    slug: String,
    viewModel: MainViewModel = viewModel()
) {
    var monster by remember { mutableStateOf<Monster?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(slug) {
        viewModel.fetchMonsterDetail(slug) { result, err ->
            monster = result
            error = err
        }
    }

    // LOADING
    if (monster == null && error == null) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // ERROR
    error?.let {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }
        return
    }

    // CONTENT
    monster?.let { m ->
        Column(Modifier.fillMaxSize().padding(16.dp)) {

            Text(m.name, style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(12.dp))

            Text("Type: ${m.type ?: "Unknown"}")
            Text("Challenge Rating: ${m.challenge_rating}")
            Text("Armor Class: ${m.armor_class}")
            Text("Hit Points: ${m.hit_points}")

            Spacer(Modifier.height(16.dp))

            Text("Abilities", style = MaterialTheme.typography.titleMedium)
            Text("STR: ${m.strength ?: 0}")
            Text("DEX: ${m.dexterity ?: 0}")
            Text("CON: ${m.constitution ?: 0}")
            Text("INT: ${m.intelligence ?: 0}")
            Text("WIS: ${m.wisdom ?: 0}")
            Text("CHA: ${m.charisma ?: 0}")

            Spacer(Modifier.height(16.dp))

            Text("Description", style = MaterialTheme.typography.titleMedium)
            Text(m.desc ?: "No description available.")
        }
    }
}
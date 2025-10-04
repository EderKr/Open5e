package com.example.open5e.ui.spells

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.open5e.MainViewModel
import com.example.open5e.models.Spell

@Composable
fun SpellsScreen(viewModel: MainViewModel = viewModel()) {
    var spells by remember { mutableStateOf<List<Spell>>(emptyList()) }
    var spellLevel by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Spell List", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = spellLevel,
            onValueChange = { spellLevel = it },
            label = { Text("Spell Level") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val level = spellLevel.toIntOrNull() ?: 0
                viewModel.fetchSpells(level) { result, error ->
                    spells = result
                    errorMessage = error
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Filter")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(spells) { spell ->
                Text(
                    text = "${spell.name} - Level: ${spell.level}" +
                            (spell.school?.let { ", School: $it" } ?: ""),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

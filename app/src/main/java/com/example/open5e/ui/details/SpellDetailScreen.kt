package com.example.open5e.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.example.open5e.models.Spell
import com.example.open5e.ui.components.TextMarkdown
import com.example.open5e.viewmodels.MainViewModel

@Composable
fun SpellDetailScreen(
    slug: String,
    viewModel: MainViewModel = viewModel()
) {
    var spell by remember { mutableStateOf<Spell?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(slug) {
        viewModel.fetchSpellDetail(slug) { result, err ->
            spell = result
            error = err
        }
    }

    if (spell == null && error == null) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    error?.let {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }
        return
    }

    spell?.let { s ->
        Column(Modifier.fillMaxSize().padding(16.dp)) {

            Text(s.name, style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(12.dp))

            TextMarkdown("Level: ${s.level}")
            TextMarkdown("School: ${s.school ?: "Unknown"}")

            Spacer(Modifier.height(16.dp))

            TextMarkdown("Casting Time: ${s.casting_time}")
            TextMarkdown("Range: ${s.range}")
            TextMarkdown("Components: ${s.components}")
            TextMarkdown("Duration: ${s.duration}")

            Spacer(Modifier.height(16.dp))

            Text("Description", style = MaterialTheme.typography.titleMedium)
            TextMarkdown(s.desc ?: "No description available.")

            s.higher_level?.let {
                Spacer(Modifier.height(16.dp))
                Text("At Higher Levels", style = MaterialTheme.typography.titleMedium)
                TextMarkdown(it)
            }
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { viewModel.saveSpellOffline(s) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Offline")
            }
        }
    }
}
package com.example.open5e.ui.spells

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.open5e.viewmodels.MainViewModel
import com.example.open5e.models.Spell

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpellsScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    var spells by remember { mutableStateOf<List<Spell>>(emptyList()) }
    var selectedLevel by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var page by remember { mutableIntStateOf(1) }
    var canBack by remember { mutableStateOf(false) }
    var canNext by remember { mutableStateOf(false) }
    var totalPages by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }

    val levels = listOf(
        "Cantrip","1st-level","2nd-level","3rd-level","4th-level","5th-level",
        "6th-level","7th-level","8th-level","9th-level"
    )

    fun loadPage() {
        isLoading = true
        viewModel.fetchSpellsPage(selectedLevel, page) { result, back, next, tp, error ->
            spells = result
            canBack = back
            canNext = next
            totalPages = tp
            errorMessage = error
            isLoading = false
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Spell List", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedLevel,
                onValueChange = {},
                readOnly = true,
                label = { Text("Spell Level") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                levels.forEach { level ->
                    DropdownMenuItem(
                        text = { Text(level) },
                        onClick = {
                            selectedLevel = level
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = { page = 1; loadPage() }, modifier = Modifier.fillMaxWidth()) {
            Text("Search")
        }

        Spacer(Modifier.height(8.dp))

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(Modifier.weight(1f)) {
                items(spells) { spell ->
                    Text(
                        "${spell.name} - ${spell.level}",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                navController.navigate("spellDetail/${spell.slug}")
                            }
                    )
                }
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { if (canBack) { page--; loadPage() } }, enabled = canBack) {
                Text("Back")
            }
            Text(if (totalPages > 0) "Página $page de $totalPages" else "Página $page")
            Button(onClick = { if (canNext) { page++; loadPage() } }, enabled = canNext) {
                Text("Next")
            }
        }

        errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }

    LaunchedEffect(Unit) { loadPage() }
}
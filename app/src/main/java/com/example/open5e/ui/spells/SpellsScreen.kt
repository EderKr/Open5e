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
import com.example.open5e.models.Spell
import com.example.open5e.viewmodels.MainViewModel

@Composable
fun SpellsScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    var selectedLevel by remember { mutableStateOf("") }
    var spells by remember { mutableStateOf<List<Spell>>(emptyList()) }

    var page by remember { mutableIntStateOf(1) }
    var totalPages by remember { mutableIntStateOf(0) }
    var canNext by remember { mutableStateOf(false) }
    var canBack by remember { mutableStateOf(false) }

    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val spellLevels = listOf(
        "", "Cantrip", "1st-level","2nd-level","3rd-level",
        "4th-level","5th-level","6th-level","7th-level","8th-level","9th-level"
    )

    fun load() {
        loading = true
        viewModel.fetchSpellsPage(selectedLevel, page) { result, back, next, tp, err ->
            spells = result
            canBack = back
            canNext = next
            totalPages = tp
            error = err
            loading = false
        }
    }

    LaunchedEffect(Unit) { load() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Text("Spells", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(8.dp))

        DropdownMenuBox(
            selected = selectedLevel,
            options = spellLevels,
            label = "Spell Level",
            onSelect = { selectedLevel = it }
        )

        Spacer(Modifier.height(8.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { page = 1; load() }
        ) {
            Text("Search")
        }

        Spacer(Modifier.height(8.dp))

        if (loading) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(Modifier.weight(1f)) {
                items(spells) { sp ->
                    Text(
                        "${sp.name} (${sp.level})",
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable { navController.navigate("spellDetail/${sp.slug}") }
                    )
                }
            }
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { if (canBack) { page--; load() } }, enabled = canBack) {
                Text("Back")
            }
            Text(if (totalPages > 0) "Page $page / $totalPages" else "Page $page")
            Button(onClick = { if (canNext) { page++; load() } }, enabled = canNext) {
                Text("Next")
            }
        }

        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(
    selected: String,
    options: List<String>,
    label: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
        )

        ExposedDropdownMenu(expanded, { expanded = false }) {
            options.forEach { op ->
                DropdownMenuItem(
                    text = { Text(op.ifBlank { "Any" }) },
                    onClick = {
                        onSelect(op)
                        expanded = false
                    }
                )
            }
        }
    }
}

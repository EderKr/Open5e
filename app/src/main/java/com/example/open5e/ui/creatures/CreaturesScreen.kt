package com.example.open5e.ui.creatures

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
import com.example.open5e.models.Monster
import com.example.open5e.viewmodels.MainViewModel

@Composable
fun CreaturesScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    var list by remember { mutableStateOf<List<Monster>>(emptyList()) }
    var crFilter by remember { mutableStateOf("") }

    var page by remember { mutableIntStateOf(1) }
    var totalPages by remember { mutableIntStateOf(0) }
    var canBack by remember { mutableStateOf(false) }
    var canNext by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    fun load() {
        isLoading = true
        viewModel.fetchMonstersPage(crFilter, page) { result, back, next, tp, err ->
            list = result
            canBack = back
            canNext = next
            totalPages = tp
            error = err
            isLoading = false
        }
    }

    LaunchedEffect(Unit) { load() }

    Column(
        Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Creatures", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        TextField(
            value = crFilter,
            onValueChange = { crFilter = it },
            label = { Text("Challenge Rating") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { page = 1; load() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Filter")
        }

        Spacer(Modifier.height(8.dp))

        if (isLoading) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(Modifier.weight(1f)) {
                items(list) { m ->
                    Text(
                        text = "${m.name} (CR ${m.challenge_rating})",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { navController.navigate("monsterDetail/${m.slug}") }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
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
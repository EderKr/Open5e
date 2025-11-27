package com.example.open5e.ui.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.open5e.viewmodels.MainViewModel
import com.example.open5e.models.MagicItem

@Composable
fun ItemDetailScreen(
    slug: String,
    viewModel: MainViewModel = viewModel()
) {
    var item by remember { mutableStateOf<MagicItem?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(slug) {
        viewModel.fetchItemDetail(slug) { result, err ->
            item = result
            error = err
        }
    }

    if (item == null && error == null) {
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

    item?.let { i ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Text(i.name, style = MaterialTheme.typography.headlineLarge)
            }

            item {
                Text("Rarity: ${i.rarity}")
                Text("Type: ${i.type}")
            }

            item {
                Text("Description", style = MaterialTheme.typography.titleMedium)
                Text(i.description ?: "No description available.")
            }
        }
    }
}
package com.example.open5e.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onCreaturesClick: () -> Unit,
    onSpellsClick: () -> Unit,
    onItemsClick: () -> Unit,
    onAccountClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    // Main layout for the home screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header title
        Text(
            text = "Welcome to Open5e",
            style = MaterialTheme.typography.headlineLarge
        )

        // Navigation buttons
        Button(
            onClick = onCreaturesClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Creatures")
        }
        Button(
            onClick = onSpellsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Spells")
        }
        Button(
            onClick = onItemsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Magic Items")
        }
        Button(
            onClick = onAccountClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("My Account")
        }
        Button(
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log Out")
        }
    }
}

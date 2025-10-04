package com.example.open5e.ui.account

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.open5e.viewmodels.AccountViewModel

@Composable
fun AccountScreen(viewModel: AccountViewModel) {
    var username by remember { mutableStateOf(viewModel.getAccountData().username) }
    var email by remember { mutableStateOf(viewModel.getAccountData().email) }
    var phone by remember { mutableStateOf(viewModel.getAccountData().phone) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "My Account", style = MaterialTheme.typography.headlineMedium)

        TextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })

        TextField(
            value = currentPassword,
            onValueChange = { currentPassword = it },
            label = { Text("Current Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        TextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                viewModel.updateAccount(username, email, phone, currentPassword, newPassword)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Apply Changes")
        }
    }
}

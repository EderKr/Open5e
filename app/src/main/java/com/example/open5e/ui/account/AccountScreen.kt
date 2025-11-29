package com.example.open5e.ui.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.open5e.viewmodels.AccountViewModel

@Composable
fun AccountScreen(viewModel: AccountViewModel) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var currentPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var success by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadAccount { ok, msg ->
            if (ok) {
                username = viewModel.account.username
                email = viewModel.account.email
                phone = viewModel.account.phone
            } else {
                error = msg
            }
        }
    }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("My Account", style = MaterialTheme.typography.headlineMedium)

        TextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })

        TextField(value = currentPass, onValueChange = { currentPass = it }, label = { Text("Current Password") }, visualTransformation = PasswordVisualTransformation())
        TextField(value = newPass, onValueChange = { newPass = it }, label = { Text("New Password") }, visualTransformation = PasswordVisualTransformation())

        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        success?.let { Text(it, color = MaterialTheme.colorScheme.primary) }

        Button(
            onClick = {
                viewModel.updateAccount(
                    username, email, phone,
                    currentPass, newPass
                ) { ok, msg ->
                    if (ok) {
                        success = "Updated successfully!"
                        error = null
                    } else {
                        error = msg
                        success = null
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Apply Changes") }
    }
}
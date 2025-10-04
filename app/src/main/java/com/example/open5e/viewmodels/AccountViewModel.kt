package com.example.open5e.viewmodels

import androidx.lifecycle.ViewModel

class AccountViewModel : ViewModel() {
    private val account = Account(username = "JohnDoe", email = "john.doe@example.com", phone = "123456789")

    fun getAccountData(): Account {
        return account
    }

    fun updateAccount(username: String, email: String, phone: String, currentPassword: String, newPassword: String) {
        // Lógica para validar e atualizar a conta (simulado aqui)
        if (currentPassword == "1234") { // Exemplo: senha fictícia
            account.username = username
            account.email = email
            account.phone = phone
            if (newPassword.isNotEmpty()) {
                // Atualiza a senha (simulado)
            }
        }
    }
}

data class Account(
    var username: String,
    var email: String,
    var phone: String
)

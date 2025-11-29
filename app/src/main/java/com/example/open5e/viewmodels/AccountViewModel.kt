package com.example.open5e.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccountViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    var account = Account("", "", "")
        private set

    fun loadAccount(onResult: (Boolean, String?) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onResult(false, "User not logged in.")
            return
        }

        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    account = Account(
                        username = doc.getString("username") ?: "",
                        email = doc.getString("email") ?: "",
                        phone = doc.getString("phone") ?: ""
                    )
                    onResult(true, null)
                } else {
                    onResult(false, "User data not found.")
                }
            }
            .addOnFailureListener {
                onResult(false, it.message)
            }
    }

    fun updateAccount(
        username: String,
        email: String,
        phone: String,
        currentPassword: String,
        newPassword: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val user = auth.currentUser
        if (user == null) {
            onResult(false, "User not logged in.")
            return
        }

        val credential = com.google.firebase.auth.EmailAuthProvider
            .getCredential(user.email ?: "", currentPassword)

        user.reauthenticate(credential)
            .addOnSuccessListener {

                val emailTask = if (email != user.email) {
                    user.updateEmail(email)
                } else null

                val passTask = if (newPassword.isNotEmpty()) {
                    user.updatePassword(newPassword)
                } else null

                val updates = mapOf(
                    "username" to username,
                    "email" to email,
                    "phone" to phone
                )

                firestore.collection("users")
                    .document(user.uid)
                    .update(updates)
                    .addOnSuccessListener {
                        account = Account(username, email, phone)
                        onResult(true, null)
                    }
                    .addOnFailureListener {
                        onResult(false, it.message)
                    }

            }
            .addOnFailureListener {
                onResult(false, "Wrong current password.")
            }
    }
}

data class Account(
    var username: String,
    var email: String,
    var phone: String
)
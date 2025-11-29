package com.example.open5e.viewmodels

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("DEPRECATION")
class AccountViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    var account = Account("", "", "")
        private set

    fun isAnonymous(): Boolean = auth.currentUser?.isAnonymous == true

    fun signInAnonymously(onResult: (Boolean, String?) -> Unit) {
        auth.signInAnonymously()
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener {
                onResult(false, it.message)
            }
    }

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
                val tasks = mutableListOf<Task<Void>>()

                if (!email.equals(user.email, ignoreCase = true)) {
                    tasks.add(user.updateEmail(email))
                }
                if (newPassword.isNotEmpty()) {
                    tasks.add(user.updatePassword(newPassword))
                }
                val doFirestoreUpdate = {
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

                if (tasks.isEmpty()) {
                    doFirestoreUpdate()
                } else {
                    Tasks.whenAll(tasks)
                        .addOnSuccessListener {
                            doFirestoreUpdate()
                        }
                        .addOnFailureListener {
                            onResult(false, it.message)
                        }
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
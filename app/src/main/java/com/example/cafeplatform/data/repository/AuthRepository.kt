package com.example.cafeplatform.data.repository

import com.example.cafeplatform.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun getUserData(uid: String, onResult: (User?) -> Unit) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                val user = doc.toObject(User::class.java)
                onResult(user)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun getAllUsers(onResult: (List<User>) -> Unit) {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val userList = result.mapNotNull { doc ->
                    doc.toObject(User::class.java)
                }
                onResult(userList)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getCurrentUserUid(): String? {
        return auth.currentUser?.uid
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        // tambahkan login Firebase Auth-mu di sini
    }
}

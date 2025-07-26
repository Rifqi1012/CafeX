package com.example.cafeplatform.data.repository

import com.example.cafeplatform.data.model.Cafe
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class CafeRepository {
    private val db = FirebaseFirestore.getInstance()
    private val cafeCollection = db.collection("cafe")

    suspend fun getAllCafe(): List<Cafe> {
        return try {
            val snapshot: QuerySnapshot = cafeCollection.get().await()
            snapshot.documents.mapNotNull { it.toObject(Cafe::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getCafeByOwnerUid(uid: String, onResult: (Cafe?) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("cafe")
            .whereEqualTo("owner_uid", uid)
            .get()
            .addOnSuccessListener { result ->
                val cafe = result.documents.firstOrNull()?.toObject(Cafe::class.java)
                onResult(cafe)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

}

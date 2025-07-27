package com.example.cafeplatform.ui.admin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Cafe(
    val nama: String = "",
    val alamat: String = "",
    val foto: String = "",
    val link_gmaps: String = "",
    val rating: Double = 0.0,
    val owner_uid: String = ""
)

class AdminHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            AdminHomeScreen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AdminHomeScreen() {
        val db = Firebase.firestore
        var cafes by remember { mutableStateOf<List<Pair<String, Cafe>>>(emptyList()) }

        // Load data dari Firestore
        LaunchedEffect(Unit) {
            db.collection("cafe")
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener { result ->
                    cafes = result.mapNotNull { doc ->
                        val cafe = Cafe(
                            nama = doc.getString("nama") ?: "",
                            alamat = doc.getString("alamat") ?: "",
                            foto = doc.getString("foto") ?: "",
                            link_gmaps = doc.getString("link_gmaps") ?: "",
                            rating = doc.getDouble("rating") ?: 0.0,
                            owner_uid = doc.getString("owner_uid") ?: ""
                        )
                        doc.id to cafe
                    }
                }
        }

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Beranda Admin") })
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Selamat Datang Admin!!",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Berikut data cafe yang perlu diterima:")
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(cafes) { (docId, cafe) ->
                        CafeCard(docId, cafe) {
                            // Setelah update, hapus dari list agar realtime refresh tanpa reload
                            cafes = cafes.filterNot { it.first == docId }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun CafeCard(docId: String, cafe: Cafe, onStatusChanged: () -> Unit) {
        val db = FirebaseFirestore.getInstance()

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Nama Cafe: ${cafe.nama}", style = MaterialTheme.typography.titleMedium)
                Text("Alamat: ${cafe.alamat}")
                Text("Rating: ${cafe.rating}")
                Text("Link Gmaps: ${cafe.link_gmaps}")
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            db.collection("cafe").document(docId)
                                .update("status", "approved")
                                .addOnSuccessListener {
                                    Toast.makeText(applicationContext, "Cafe diterima", Toast.LENGTH_SHORT).show()
                                    onStatusChanged()
                                }
                        }
                    ) {
                        Text("Terima")
                    }

                    Button(
                        onClick = {
                            db.collection("cafe").document(docId)
                                .update("status", "rejected")
                                .addOnSuccessListener {
                                    Toast.makeText(applicationContext, "Cafe ditolak", Toast.LENGTH_SHORT).show()
                                    onStatusChanged()
                                }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Tolak")
                    }
                }
            }
        }
    }
}


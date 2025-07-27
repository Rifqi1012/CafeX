package com.example.cafeplatform.ui.cafe

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeplatform.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CafeFormActivity : AppCompatActivity() {

    private lateinit var etNamaKafe: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etRating: EditText
    private lateinit var etMaps: EditText
    private lateinit var btnProses: Button

    private val db = FirebaseFirestore.getInstance()
    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: "default_uid"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafe_form)

        etNamaKafe = findViewById(R.id.etNamaKafe)
        etAlamat = findViewById(R.id.etAlamat)
        etRating = findViewById(R.id.etRating)
        etMaps = findViewById(R.id.etMaps)
        btnProses = findViewById(R.id.btnProses)

        btnProses.setOnClickListener {
            simpanDataKeFirestore()
        }
    }

    private fun simpanDataKeFirestore() {
        val nama = etNamaKafe.text.toString().trim()
        val alamat = etAlamat.text.toString().trim()
        val ratingStr = etRating.text.toString().trim()
        val linkGmaps = etMaps.text.toString().trim()

        if (nama.isEmpty() || alamat.isEmpty() || ratingStr.isEmpty() || linkGmaps.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua data", Toast.LENGTH_SHORT).show()
            return
        }

        val rating = ratingStr.toDoubleOrNull()
        if (rating == null || rating < 0 || rating > 5) {
            Toast.makeText(this, "Rating harus antara 0 - 5", Toast.LENGTH_SHORT).show()
            return
        }

        val dataCafe = hashMapOf(
            "nama" to nama,
            "alamat" to alamat,
            "rating" to rating,
            "link_gmaps" to linkGmaps,
            "foto" to "https://images.unsplash.com/photo-1509042239860-f550ce710b93?auto=format&fit=crop&w=800&q=80", // sementara link hardcoded
            "owner_uid" to currentUserUid
        )

        db.collection("cafe")
            .add(dataCafe)
            .addOnSuccessListener {
                Toast.makeText(this, "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                finish() // Tutup form
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

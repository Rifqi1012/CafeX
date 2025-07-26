package com.example.cafeplatform.data.model

data class User(
    val email: String = "",
    val role: String = "",      // "admin" atau "cafe"
    val status: String = "",    // "aktif", "menunggu", "ditolak", dst
    val nama: String = ""       // Nama pengguna / cafe owner
)

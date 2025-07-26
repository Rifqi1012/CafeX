package com.example.cafeplatform.data.model

data class Cafe(
    val nama: String = "",
    val alamat: String = "",
    val rating: Int = 0,
    val link_gmaps: String = "",
    val foto: String? = null,
    val owner_uid: String = ""
)
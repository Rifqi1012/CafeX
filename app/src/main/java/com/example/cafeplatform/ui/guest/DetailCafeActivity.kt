package com.example.cafeplatform

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeplatform.databinding.ActivityDetailCafeBinding
import coil.load

class DetailCafeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCafeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCafeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cafeName = intent.getStringExtra("name")
        val address = intent.getStringExtra("address")
        val rating = intent.getDoubleExtra("rating", 0.0)
        val imageUrl = intent.getStringExtra("imageUrl")
        val mapUrl = intent.getStringExtra("mapUrl") // ambil link maps

        binding.tvCafeName.text = cafeName
        binding.tvAddress.text = address
        binding.tvRating.text = rating.toString()
        binding.ivCafe.load(imageUrl) {
            placeholder(R.drawable.cafexlogonobg)
            error(R.drawable.cafexlogonobg)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        // Buka Google Maps saat klik tombol
        binding.btnMaps.setOnClickListener {
            mapUrl?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            }
        }
    }
}

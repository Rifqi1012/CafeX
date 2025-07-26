package com.example.cafeplatform.ui.cafe

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeplatform.viewmodel.CafeViewModel
import com.example.cafeplatform.databinding.ActivityCafeHomeBinding

class CafeHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCafeHomeBinding
    private val viewModel: CafeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCafeHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            viewModel.getCafeByOwner(uid)
        }

        viewModel.cafeData.observe(this) { cafe ->
            if (cafe != null) {
                binding.tvCafeName.text = cafe.nama
                binding.tvCafeAddress.text = cafe.alamat
                binding.tvCafeRating.text = "Rating: ${cafe.rating}"
            } else {
                Toast.makeText(this, "Data cafe tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

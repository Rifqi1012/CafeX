package com.example.cafeplatform

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeplatform.ui.admin.AdminLoginActivity


class SplashActivity : AppCompatActivity() {
    private var tapCount = 0
    private var lastTapTime: Long = 0L
    private lateinit var logoImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        logoImageView = findViewById(R.id.imageViewLogo)

        // Cek tap untuk trigger admin
        logoImageView.setOnClickListener {
            val currentTime = System.currentTimeMillis()

            // Reset jika lebih dari 1 detik antar tap
            if (currentTime - lastTapTime > 1000) {
                tapCount = 0
            }

            tapCount++
            lastTapTime = currentTime

            if (tapCount >= 5) {
                tapCount = 0
                Toast.makeText(this, "Admin mode activated", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AdminLoginActivity::class.java))
            }
        }

        // Tetap arahkan ke MainActivity (untuk user) setelah 2 detik
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }
}

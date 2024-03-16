package com.example.bin_rush

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)
        val action = supportActionBar
        action?.hide()
        imageView.setOnClickListener {
            startActivity(Intent(this@MainActivity, PlayActivity::class.java))
        }
    }
}
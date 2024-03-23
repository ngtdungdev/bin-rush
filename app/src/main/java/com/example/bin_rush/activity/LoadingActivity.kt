package com.example.bin_rush.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.bin_rush.R

class LoadingActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            startActivity(Intent(this@LoadingActivity, MainActivity::class.java))
            finish()
        }
        handler.postDelayed(runnable, 5000)
    }
}
package com.example.bin_rush

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var progressBar: ProgressBar
    private var countDownTimer: CountDownTimer? = null
    private lateinit var number: TextView
    private val initialTimeInMillis: Long = 60 * 60 * 1000
    private lateinit var btnPlay: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)
        number = findViewById(R.id.number)
        btnPlay = findViewById(R.id.btnPlay)
        progressBar = findViewById(R.id.progressBar)
        progressBar.max = initialTimeInMillis.toInt()
        val action = supportActionBar
        action?.hide()
        imageView.setOnClickListener {
            startActivity(Intent(this@MainActivity, PlayActivity::class.java))
        }
        btnPlay.setOnClickListener {
            PlayGameFragment().show(supportFragmentManager , "PlayGameFragment")
        }
        startTimer(initialTimeInMillis)
    }

    private fun startTimer(timeInMillis: Long) {
        countDownTimer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateCountDownText(millisUntilFinished)
                progressBar.progress = initialTimeInMillis.toInt() - millisUntilFinished.toInt()
            }

            override fun onFinish() {
                number.text = (number.text.toString().toInt() + 1).toString()
                progressBar.progress = 0
                startTimer(initialTimeInMillis)
            }
        }.start()
    }

    private fun updateCountDownText(millisUntilFinished: Long) {
        val minutes = (millisUntilFinished / 1000) / 60
        val seconds = (millisUntilFinished / 1000) % 60
        val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        findViewById<TextView>(R.id.times).text = timeFormatted
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
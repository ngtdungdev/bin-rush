package com.example.bin_rush.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.bin_rush.fragment.PlayGameFragment
import com.example.bin_rush.R
import com.example.bin_rush.fragment.DateTimeFragment
import com.example.bin_rush.util.HeartDecreaseListener
import com.example.bin_rush.util.HeartUpdateListener
import com.example.bin_rush.util.OnFragmentListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Locale

class MainActivity : AppCompatActivity(), HeartDecreaseListener, HeartUpdateListener,  OnFragmentListener{
    private var treeLevel = 1
    private var remainingHearts = 3
    private var heartRecoveryCount = 0
    private val MAX_HEARTS = 3
    private val MAX_WATER = 5
    private var value = 0
    private var num = 0
    private lateinit var countDownTimerHeart: CountDownTimer
    private lateinit var imageView: ImageView
    private lateinit var progressBar: ProgressBar
    private var countDownTimer: CountDownTimer? = null
    private lateinit var number: TextView
    private lateinit var numberWater: TextView
    private lateinit var description: TextView
    private val initialTimeInMillis: Long = 1 * 10 * 1000
    private lateinit var btnPlay: ImageView
    private lateinit var btnDaily: ImageView
    private lateinit var imageTree: ImageView
    private lateinit var imageWater: ImageView
    private lateinit var experience: ProgressBar
    private var isTimerRunning = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.image)
        number = findViewById(R.id.number)
        numberWater = findViewById(R.id.numberWater)
        description = findViewById(R.id.description)
        btnPlay = findViewById(R.id.btnPlay)
        progressBar = findViewById(R.id.progressBar)
        imageTree = findViewById(R.id.imageTree)
        imageWater =  findViewById(R.id.imageWater)
        btnDaily = findViewById(R.id.btnDaily)
        experience = findViewById(R.id.experience)
        progressBar.max = initialTimeInMillis.toInt()
        initView()
    }

    private lateinit var database: DatabaseReference
    @SuppressLint("SetTextI18n")
    private fun initView() {
        val action = supportActionBar
        action?.hide()
        Glide.with(this)
            .asGif()
            .load(R.drawable.benefit)
            .into(btnDaily)
        Glide.with(this)
            .asGif()
            .load(getTreeGif(treeLevel))
            .into(imageTree)
        Glide.with(this)
            .asGif()
            .load(R.drawable.water_drop)
            .into(imageWater)
        experience.max = 5
        experience.progress = 0
        imageView.setOnClickListener {
            startActivity(Intent(this@MainActivity, PlayActivity::class.java))
        }

        imageWater.setOnClickListener {
            if(value > 0) {
                num = numberWater.text.toString().toIntOrNull() ?: 0
                numberWater.text = (value + num).toString()
                number.text = "0"
                value = 0
                if (!isTimerRunning) {
                    startTimer(initialTimeInMillis)
                }
            }
        }
        description.text = "It takes " + getExperient() + " times to level up"
        btnPlay.setOnClickListener {
            num = numberWater.text.toString().toIntOrNull() ?: 0 //lấy CSDL
            if(num > 0){
                num--
                numberWater.text = num.toString()
                experience.progress++
                if(experience.progress == experience.max){
                    treeLevel++
                    Glide.with(this)
                        .asGif()
                        .load(getTreeGif(treeLevel))
                        .into(imageTree)
                    experience.max = getNextMaxExperience(treeLevel)
                    experience.progress = 0
                }
                description.text = "It takes " + getExperient() + " times to level up"
            } else {
                val fragment = PlayGameFragment()
                val args = Bundle()
                args.putInt("remainingHearts", remainingHearts)
                fragment.arguments = args
                fragment.show(supportFragmentManager, "PlayGameFragment")
            }

        }
        btnDaily.setOnClickListener {
            DateTimeFragment().show(supportFragmentManager , "DateTimeFragment")
        }
        database = Firebase.database.getReference("const")
        startTimer(initialTimeInMillis)
        startHeartRecoveryTimer()
    }

    private fun getExperient(): Int {
        return experience.max - experience.progress
    }

    private fun getTreeGif(level: Int): Int {
        return when(level) {
            1 -> R.drawable.lvl1
            2 -> R.drawable.lvl2
            3 -> R.drawable.lvl3
            4 -> R.drawable.lvl4
            else -> R.drawable.lvl4
        }
    }

    private fun getNextMaxExperience(level: Int): Int {
        return when(level) {
            1 -> 5
            2 -> 25
            3 -> 60
            4 -> 100
            else -> 0
        }
    }

    private fun startHeartRecoveryTimer() {
        if (remainingHearts < MAX_HEARTS) {
            countDownTimerHeart = object : CountDownTimer(initialTimeInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {}

                override fun onFinish() {
                    recoverHeart()
                    if (remainingHearts < MAX_HEARTS) {
                        startHeartRecoveryTimer()
                    }
                }
            }
            countDownTimerHeart.start()
        }
    }

    private fun recoverHeart() {
        if (remainingHearts < MAX_HEARTS) {
            remainingHearts++
            heartRecoveryCount++
            updateHeartsVisibility()
        }
    }

    override fun decreaseHearts() {
        updateRemainingHearts(--remainingHearts)
        updateHeartsVisibility()
    }

    fun updateRemainingHearts(newRemainingHearts: Int) {
        remainingHearts = newRemainingHearts
        updateHeartsVisibility()
        startHeartRecoveryTimer()
    }

    override fun updateHeartsVisibility() {
        val imageHeart1 = findViewById<ImageView>(R.id.imageHeart1)
        val imageUnHeart1 = findViewById<ImageView>(R.id.imageUnHeart1)
        val imageHeart2 = findViewById<ImageView>(R.id.imageHeart2)
        val imageUnHeart2 = findViewById<ImageView>(R.id.imageUnHeart2)
        val imageHeart3 = findViewById<ImageView>(R.id.imageHeart3)
        val imageUnHeart3 = findViewById<ImageView>(R.id.imageUnHeart3)

        when (remainingHearts) {
            3 -> {
                imageHeart1.visibility = View.VISIBLE
                imageHeart2.visibility = View.VISIBLE
                imageHeart3.visibility = View.VISIBLE
                imageUnHeart1.visibility = View.GONE
                imageUnHeart2.visibility = View.GONE
                imageUnHeart3.visibility = View.GONE
            }
            2 -> {
                imageHeart1.visibility = View.VISIBLE
                imageHeart2.visibility = View.VISIBLE
                imageHeart3.visibility = View.GONE
                imageUnHeart1.visibility = View.GONE
                imageUnHeart2.visibility = View.GONE
                imageUnHeart3.visibility = View.VISIBLE
            }
            1 -> {
                imageHeart1.visibility = View.VISIBLE
                imageHeart2.visibility = View.GONE
                imageHeart3.visibility = View.GONE
                imageUnHeart1.visibility = View.GONE
                imageUnHeart2.visibility = View.VISIBLE
                imageUnHeart3.visibility = View.VISIBLE
            }
            else -> {
                imageHeart1.visibility = View.GONE
                imageHeart2.visibility = View.GONE
                imageHeart3.visibility = View.GONE
                imageUnHeart1.visibility = View.VISIBLE
                imageUnHeart2.visibility = View.VISIBLE
                imageUnHeart3.visibility = View.VISIBLE
            }
        }
    }

    private fun getLevelUp(id : Int) {
        database.child("const").child("_TREE_LEVELS")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val levelUpData = dataSnapshot.child("level_up").value.toString()
                        // Update UI or perform actions based on levelUpData
                        Log.i("com", levelUpData)
                    } else {
                        Log.i("com", "Level Up data does not exist")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.i("com", "Error getting level up data", databaseError.toException())
                }
            })
    }
    private fun startTimer(timeInMillis: Long) {
        if (value < MAX_WATER) {
            isTimerRunning = true
            countDownTimer = object : CountDownTimer(timeInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    updateCountDownText(millisUntilFinished)
                    progressBar.progress = initialTimeInMillis.toInt() - millisUntilFinished.toInt()
                }

                @SuppressLint("SetTextI18n")
                override fun onFinish() {
                    isTimerRunning = false
                    number.text = (number.text.toString().toInt() + 1).toString()
                    value = number.text.toString().toIntOrNull() ?: 0
                    progressBar.progress = 0
                    startTimer(initialTimeInMillis)
                }
            }.start()
        }
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
        countDownTimerHeart.cancel()
    }

    override fun onFragmentListener(data: Int) {
        number.text = (number.text.toString().toInt() + data).toString()
    }
}
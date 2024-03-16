package com.example.bin_rush

import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text

class PlayActivity: AppCompatActivity() {
    var candies = intArrayOf(
        R.drawable.bluecandy,
        R.drawable.greencandy,
        R.drawable.orangecandy,
        R.drawable.purplecandy,
        R.drawable.redcandy,
        R.drawable.yellowcandy

    )
    var widthOfBlock:Int = 0
    var noOfBlock:Int = 8
    var withOfScreen:Int = 0
    lateinit var candy: ArrayList<ImageView>
    var candyToBeDragged: Int = 0
    var candyToBeReplaced: Int = 0
    var notCandy: Int  = R.drawable.transparent

    lateinit var mHandler: Handler
    private lateinit var scoreResult: TextView
    var score = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

    }
}
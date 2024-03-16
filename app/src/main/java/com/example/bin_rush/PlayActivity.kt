package com.example.bin_rush

import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Arrays.asList

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
    var widthOfScreen:Int = 0
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

        scoreResult = findViewById(R.id.score)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        widthOfScreen = displayMetrics.widthPixels

        var heightOfScreen = displayMetrics.heightPixels
        widthOfBlock = widthOfScreen / noOfBlock
        candy = ArrayList()
        createBoard()

        for(imageView in candy) {

            imageView.setOnTouchListener(object :OnSwipeListener(this){
                override fun onSwipeLeft() {
                    super.onSwipeLeft()
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged - 1
                    candyInterChange()
                }

                override fun onSwipeRight() {
                    super.onSwipeRight()
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged + 1
                    candyInterChange()
                }

                override fun onSwipeTop() {
                    super.onSwipeTop()
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged - noOfBlock
                    candyInterChange()
                }

                override fun onSwipeBottom() {
                    super.onSwipeBottom()
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged + noOfBlock
                    candyInterChange()
                }

            })

        }
        mHandler = Handler()
        startRepeat()
    }

    private fun startRepeat() {
        var background: Int = candy.get(candyToBeReplaced).tag as Int
        var background1: Int = candy.get(candyToBeReplaced).tag as Int

        candy.get(candyToBeDragged).setImageResource(background)
        candy.get(candyToBeDragged).setImageResource(background1)

        candy.get(candyToBeDragged).setTag(background1)
        candy.get(candyToBeDragged).setTag(background1)
    }

    private fun checkRowForThree() {
        for(i in 0..61) {
            var chosedCandy = candy.get(i).tag
            var isBlank: Boolean = candy.get(i).tag == notCandy
            val notValid = arrayOf(6,7,14,15,22,23,30,31,38,39,46,47,54,55)
            val list = asList(*notValid)

            if(!list.contains(i)) {
                var x = i

                if(candy.get(x++).tag as Int == chosedCandy
                    && !isBlank
                    && candy.get(x++).tag as Int == chosedCandy
                    && candy.get(x).tag as Int == chosedCandy
                ){
                    score = score + 3
                    scoreResult.text = "$score"
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                    x--
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                }
            }
        }
    }

    private fun candyInterChange() {
        var background: Int = candy.get(candyToBeReplaced).tag as Int
        var background1: Int = candy.get(candyToBeDragged).tag as Int

        // To be swapped
        candy.get(candyToBeDragged).setImageResource(background)
        candy.get(candyToBeReplaced).setImageResource(background1)

        candy.get(candyToBeDragged).setTag(background)
        candy.get(candyToBeReplaced).setTag(background1)
    }

    private fun checkColumnForThree() {
        for (i in 0..47) {
            var chosenCandy = candy.get(i).tag
            var isBlank: Boolean = candy.get(i).tag == notCandy
            var x = i
                if (candy.get(x).tag as Int == chosenCandy
                    && !isBlank
                    && candy.get(x+noOfBlock).tag as Int == chosenCandy
                    && candy.get(x+2*noOfBlock).tag as Int == chosenCandy
                ) {
                    score = score + 3
                    scoreResult.text = "$score"
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                    x = x + noOfBlock
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                    x = x + noOfBlock
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                }
        }
//        moveDownCandies()
    }

    private fun createBoard() {
        TODO("Not yet implemented")
    }
}
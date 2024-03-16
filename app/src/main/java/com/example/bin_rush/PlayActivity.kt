package com.example.bin_rush

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.gridlayout.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Arrays.asList
import kotlin.math.abs
import kotlin.math.floor

class PlayActivity: AppCompatActivity() {
    private var candies = intArrayOf(
        R.drawable.apple,
        R.drawable.banana,
        R.drawable.bottle,
        R.drawable.metal,
        R.drawable.plastic,
        R.drawable.paper
    )
    var widthOfBlock:Int = 0
    var noOfBlock:Int = 8

    var widthOfScreen:Int = 0
    lateinit var candy: ArrayList<ImageView>
    var candyToBeDragged: Int = 0
    var candyToBeReplaced: Int = 0
    var notCandy: Int  = R.drawable.transparent
    lateinit var gridlayout: GridLayout

    lateinit var mHandler: Handler
    lateinit var scoreResult: TextView
    var heightOfScreen: Int = 0
    var score = 0
    var interval = 100L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        scoreResult = findViewById(R.id.score)
        gridlayout = findViewById(R.id.board)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        widthOfScreen = displayMetrics.widthPixels

        heightOfScreen = widthOfScreen + 10
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
        repeatChecker.run()
    }

    private fun checkRowForThree() {
        for(i in 0..61) {
            var chosenCandy = candy[i].tag
            var isBlank: Boolean = candy[i].tag == notCandy
            val notValid = arrayOf(6,7,14,15,22,23,30,31,38,39,46,47,54,55)
            val list = listOf(*notValid)
            if(!list.contains(i)) {
                var x = i

                if(candy[x++].tag as Int == chosenCandy
                    && !isBlank
                    && candy[x++].tag as Int == chosenCandy
                    && candy[x].tag as Int == chosenCandy
                ){
                    score += 3
                    scoreResult.text = "$score"
                    candy[x].setImageResource(notCandy)
                    candy[x].tag = notCandy
                    x--
                    candy[x].setImageResource(notCandy)
                    candy[x].tag = notCandy
                    x--
                    candy[x].setImageResource(notCandy)
                    candy[x].tag = notCandy
                }
            }
        }
        moveDownCandies()
    }

    private fun candyInterChange() {
        var background: Int = candy[candyToBeReplaced].tag as Int
        var background1: Int = candy[candyToBeDragged].tag as Int

        // To be swapped
        candy[candyToBeDragged].setImageResource(background)
        candy[candyToBeReplaced].setImageResource(background1)

        candy[candyToBeDragged].tag = background
        candy[candyToBeReplaced].tag = background1
    }

    private fun checkColumnForThree() {
        for (i in 0..47) {
            var chosenCandy = candy[i].tag
            var isBlank: Boolean = candy[i].tag == notCandy
            var x = i
            if (candy[x].tag as Int == chosenCandy
                && !isBlank
                && candy[x+noOfBlock].tag as Int == chosenCandy
                && candy[x+2*noOfBlock].tag as Int == chosenCandy
            ) {
                score += 3
                scoreResult.text = "$score"
                candy[x].setImageResource(notCandy)
                candy[x].tag = notCandy
                x += noOfBlock
                candy[x].setImageResource(notCandy)
                candy[x].tag = notCandy
                x += noOfBlock
                candy[x].setImageResource(notCandy)
                candy[x].tag = notCandy
            }
        }
        moveDownCandies()
    }

    private fun moveDownCandies() {
        val firstRow = arrayOf(1, 2, 3, 4, 5, 6, 7)
        val list = listOf(*firstRow)
        for (i in 55 downTo 0) {
            if (candy[i+noOfBlock].tag as Int == notCandy) {
                candy[i+noOfBlock].setImageResource(candy[i].tag as Int)
                candy[i+noOfBlock].tag = candy[i].tag as Int

                candy[i].setImageResource(notCandy)
                candy[i].tag = notCandy
                if (list.contains(i) && candy[i].tag == notCandy) {
                    var randomColor: Int = abs(Math.random() * candies.size).toInt()
                    candy[i].setImageResource(candies[randomColor])
                    candy[i].tag = candies[randomColor]
                }
            }
        }
        for (i in 0..7) {
            if (candy[i].tag as Int == notCandy) {
                var randomColor: Int = Math.abs(Math.random() * candies.size).toInt()
                candy[i].setImageResource(candies[randomColor])
                candy[i].tag = candies[randomColor]
            }
        }
    }

    val repeatChecker: Runnable = object: Runnable{
        override fun run() {
            try {
                checkRowForThree()
                checkColumnForThree()
                moveDownCandies()
            }
            finally {
                mHandler.postDelayed(this, interval)
            }
        }
    }
    private fun createBoard() {
        gridlayout.rowCount = noOfBlock
        gridlayout.columnCount = noOfBlock
        gridlayout.layoutParams.width = widthOfScreen
        gridlayout.layoutParams.height = heightOfScreen
        for ( i in 0 until 64) {
            val imageView = ImageView(this)
            imageView.id = i
            val sizeInPixels = convertDpToPixel(50f, this)
            val layoutParams = GridLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams(sizeInPixels, sizeInPixels)
            )
            val margin = 2
            layoutParams.setMargins(margin, margin, margin, margin)
            imageView.layoutParams = layoutParams

            var random: Int = floor(Math.random() * candies.size).toInt()
            imageView.setImageResource(candies[random])
            imageView.tag = candies[random]
            candy.add(imageView)
            gridlayout.addView(imageView)
        }
    }

    private fun convertDpToPixel(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}
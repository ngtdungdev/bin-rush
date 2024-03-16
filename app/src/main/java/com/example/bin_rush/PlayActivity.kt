package com.example.bin_rush

import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import androidx.gridlayout.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Arrays.asList

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

        heightOfScreen = displayMetrics.heightPixels
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
        moveDownCandies()
    }

    private fun moveDownCandies() {
        val firstRow = arrayOf(1, 2, 3, 4, 5, 6, 7)
        val list = asList(*firstRow)
        for (i in 55 downTo 0) {
            if (candy.get(i+noOfBlock).tag as Int == notCandy) {
                candy.get(i+noOfBlock).setImageResource(candy.get(i).tag as Int)
                candy.get(i+noOfBlock).setTag(candy.get(i).tag as Int)

                candy.get(i).setImageResource(notCandy)
                candy.get(i).setTag(notCandy)
                if (list.contains(i) && candy.get(i).tag == notCandy) {
                    var randomColor: Int = Math.abs(Math.random() * candies.size).toInt()
                    candy.get(i).setImageResource(candies[randomColor])
                    candy.get(i).setTag(candies[randomColor])
                }
            }
        }
        for (i in 0..7) {
            if (candy.get(i).tag as Int == notCandy) {
                var randomColor: Int = Math.abs(Math.random() * candies.size).toInt()
                candy.get(i).setImageResource(candies[randomColor])
                candy.get(i).setTag(candies[randomColor])
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
            imageView.layoutParams = android.view.ViewGroup.LayoutParams(widthOfBlock, widthOfBlock)
            var random: Int = Math.floor(Math.random() * candies.size).toInt()
            imageView.setImageResource(candies[random])
            imageView.setTag(candies[random])
            candy.add(imageView)
            gridlayout.addView(imageView)
        }
    }
}
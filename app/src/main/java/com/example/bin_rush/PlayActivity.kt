package com.example.bin_rush

import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import androidx.gridlayout.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs
import kotlin.math.floor

class PlayActivity: AppCompatActivity() {
    private var WASTE_TYPE = intArrayOf(
        R.drawable.apple,
        R.drawable.banana,
        R.drawable.bottle,
        R.drawable.metal,
        R.drawable.plastic,
        R.drawable.paper

    )
    var widthOfBlock: Int = 0
    var noOfBlock: Int = 8

    var widthOfScreen: Int = 0
    var heightOfScreen: Int = 0

    lateinit var wastes: ArrayList<ImageView>
    var candyToBeDragged: Int = 0
    var candyToBeReplaced: Int = 0
    var notCandy: Int  = R.drawable.transparent

    lateinit var gridlayout: GridLayout
    lateinit var mHandler: Handler
    lateinit var scoreResult: TextView
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

        wastes = ArrayList()
        createBoard()

        for (imageView in wastes) {
            imageView.setOnTouchListener(object: OnSwipeListener(this) {
                override fun onSwipeLeft() {
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged - 1
                    swapBlocks()
                }

                override fun onSwipeRight() {
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged + 1
                    swapBlocks()
                }

                override fun onSwipeTop() {
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged - noOfBlock
                    swapBlocks()
                }

                override fun onSwipeBottom() {
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged + noOfBlock
                    swapBlocks()
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
        for (i in 0..61) {
            val chosenCandy = wastes[i].tag
            val isBlank: Boolean = wastes[i].tag == notCandy
            val notValid = arrayOf(6, 7, 14, 15, 22, 23, 30, 31, 38, 39, 46, 47, 54, 55)
            val list = listOf(*notValid)
            if (!list.contains(i)) {
                var x = i

                if (wastes[x++].tag as Int == chosenCandy
                    && !isBlank
                    && wastes[x++].tag as Int == chosenCandy
                    && wastes[x].tag as Int == chosenCandy
                ) {
                    score += 3
                    scoreResult.text = "$score"
                    wastes[x].setImageResource(notCandy)
                    wastes[x].tag = notCandy
                    x--
                    wastes[x].setImageResource(notCandy)
                    wastes[x].tag = notCandy
                    x--
                    wastes[x].setImageResource(notCandy)
                    wastes[x].tag = notCandy
                }
            }
        }
        moveDownCandies()
    }

    private fun checkColumnForThree() {
        for (i in 0..47) {
            val chosenCandy = wastes[i].tag
            val isBlank: Boolean = wastes[i].tag == notCandy
            var x = i
                if (wastes[x].tag as Int == chosenCandy
                    && !isBlank
                    && wastes[x + noOfBlock].tag as Int == chosenCandy
                    && wastes[x + 2*noOfBlock].tag as Int == chosenCandy
                ) {
                    score += 3
                    scoreResult.text = "$score"
                    wastes[x].setImageResource(notCandy)
                    wastes[x].tag = notCandy
                    x += noOfBlock
                    wastes[x].setImageResource(notCandy)
                    wastes[x].tag = notCandy
                    x += noOfBlock
                    wastes[x].setImageResource(notCandy)
                    wastes[x].tag = notCandy
                }
        }
        moveDownCandies()
    }

    private fun moveDownCandies() {
        val firstRow: List<Int> = List(noOfBlock) { it }
        for (i in 55 downTo 0) {
            if (wastes[i + noOfBlock].tag as Int == notCandy) {
                wastes[i + noOfBlock].setImageResource(wastes[i].tag as Int)
                wastes[i + noOfBlock].tag = wastes[i].tag as Int

                wastes[i].setImageResource(notCandy)
                wastes[i].tag = notCandy

                // if the first row is blank
                if (firstRow.contains(i) && wastes[i].tag == notCandy) {
                    val randomColor: Int = abs(Math.random() * WASTE_TYPE.size).toInt()
                    wastes[i].setImageResource(WASTE_TYPE[randomColor])
                    wastes[i].tag = WASTE_TYPE[randomColor]
                }
            }
        }
        for (i in firstRow) {
            if (wastes[i].tag as Int == notCandy) {
                val randomColor: Int = abs(Math.random() * WASTE_TYPE.size).toInt()
                wastes[i].setImageResource(WASTE_TYPE[randomColor])
                wastes[i].tag = WASTE_TYPE[randomColor]
            }
        }
    }

    val repeatChecker: Runnable = object: Runnable {
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

    private fun swapBlocks() {
        val background: Int = wastes[candyToBeReplaced].tag as Int
        val background1: Int = wastes[candyToBeDragged].tag as Int

        wastes[candyToBeDragged].setImageResource(background)
        wastes[candyToBeDragged].tag = background

        wastes[candyToBeReplaced].setImageResource(background1)
        wastes[candyToBeReplaced].tag = background1
    }

    private fun createBoard() {
        gridlayout.rowCount = noOfBlock
        gridlayout.columnCount = noOfBlock
        gridlayout.layoutParams.width = widthOfScreen
        gridlayout.layoutParams.height = heightOfScreen
        for (i in 0 until noOfBlock*noOfBlock) {
            val imageView = ImageView(this)
            imageView.id = i
            imageView.layoutParams = android.view.ViewGroup.LayoutParams(widthOfBlock, widthOfBlock)
            val random: Int = floor(Math.random() * WASTE_TYPE.size).toInt()
            imageView.setImageResource(WASTE_TYPE[random])
            imageView.tag = WASTE_TYPE[random]
            wastes.add(imageView)
            gridlayout.addView(imageView)
        }
    }
}
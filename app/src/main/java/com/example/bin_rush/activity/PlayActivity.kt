package com.example.bin_rush.activity

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import androidx.gridlayout.widget.GridLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bin_rush.R
import com.example.bin_rush.util.OnSwipeListener
import kotlin.math.abs
import kotlin.math.floor

class PlayActivity: AppCompatActivity() {
    private val WASTE_TYPE = intArrayOf(
        R.drawable.apple,
        R.drawable.banana,
        R.drawable.bottle,
        R.drawable.metal,
        R.drawable.plastic,
        R.drawable.paper_super,
        R.drawable.vaccine,
    )
    private val classification = mapOf(
        R.drawable.apple to 2,
        R.drawable.banana to 2,
        R.drawable.bottle to 4,
        R.drawable.metal to 4,
        R.drawable.plastic to 1,
        R.drawable.paper_super to 1,
        R.drawable.vaccine to 3,
    )

    var widthOfBlock: Int = 0
    var noOfBlock: Int = 8

    var widthOfScreen: Int = 0
    var heightOfScreen: Int = 0

    var emptyBlock: Int  = R.drawable.transparent
    lateinit var wastes: ArrayList<ImageView>
    var blockStart: Int = 0
    var blockEnd: Int = 0
    private lateinit var imageInorganic: ImageView
    private lateinit var imageOrganic: ImageView
    private lateinit var imageBiohazard: ImageView
    private lateinit var imageRecycle: ImageView
    private lateinit var layout: View
    private lateinit var frameLayout: FrameLayout
    private lateinit var imageClock: ImageView
    private lateinit var progressBar: ProgressBar

    private var timerDuration: Long = 20000
    private var countDownInterval: Long = 1000
    lateinit var gridlayout: GridLayout
    lateinit var mHandler: Handler
    lateinit var scoreResult: TextView
    var score = 0
    var interval = 200L
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        progressBar = findViewById(R.id.progressBar)
        scoreResult = findViewById(R.id.score)
        gridlayout = findViewById(R.id.board)
        imageBiohazard = findViewById(R.id.imageBiohazard)
        imageInorganic = findViewById(R.id.imageInorganic)
        imageOrganic = findViewById(R.id.imageOrganic)
        imageRecycle = findViewById(R.id.imageRecycle)
        layout = findViewById(R.id.LinearLayout)
        frameLayout = findViewById(R.id.frameLayout)
        imageClock = findViewById(R.id.imageClock)


        Glide.with(this)
            .asGif()
            .load(R.drawable.clock)
            .into(imageClock)

        progressBar.max = (timerDuration / countDownInterval).toInt()
        startCountDown(timerDuration, countDownInterval)


        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        widthOfScreen = displayMetrics.widthPixels

        heightOfScreen = widthOfScreen + 10
        widthOfBlock = widthOfScreen / noOfBlock

        wastes = ArrayList()
        createBoard()

        for (imageView in wastes) {
            imageView.setOnTouchListener(object: OnSwipeListener(this) {
                override fun onSwipeLeft() {
                    val firstColumn: List<Int> = (0 until noOfBlock*noOfBlock).filter { it % noOfBlock == 0 }.toList()
                    if (firstColumn.contains(imageView.id)) {
                        return
                    }
                    blockStart = imageView.id
                    blockEnd = blockStart - 1
                    swapBlocks()
                    val case1 = check(blockStart);
                    val case2 = check(blockEnd);
                    if (!case1 && !case2) {
                        swapBlocks()
                    }
                }

                override fun onSwipeRight() {
                    val lastColumn: List<Int> = (0 until noOfBlock*noOfBlock).filter { it % noOfBlock == noOfBlock - 1 }.toList()
                    if (lastColumn.contains(imageView.id)) {
                        return
                    }
                    blockStart = imageView.id
                    blockEnd = blockStart + 1
                    swapBlocks()
                    val case1 = check(blockStart);
                    val case2 = check(blockEnd);
                    if (!case1 && !case2) {
                        swapBlocks()
                    }
                }

                override fun onSwipeTop() {
                    val firstRow: List<Int> = (0 until noOfBlock).toList()
                    if (firstRow.contains(imageView.id)) {
                        return
                    }
                    blockStart = imageView.id
                    blockEnd = blockStart - noOfBlock
                    swapBlocks()
                    val case1 = check(blockStart);
                    val case2 = check(blockEnd);
                    if (!case1 && !case2) {
                        swapBlocks()
                    }
                }

                override fun onSwipeBottom() {
                    val lastElement = noOfBlock*noOfBlock - 1
                    val lastRow: List<Int> = (lastElement downTo lastElement - noOfBlock).toList()
                    if (lastRow.contains(imageView.id)) {
                        return
                    }
                    blockStart = imageView.id
                    blockEnd = blockStart + noOfBlock
                    swapBlocks()
                    val case1 = check(blockStart);
                    val case2 = check(blockEnd);
                    if (!case1 && !case2) {
                        swapBlocks()
                    }
                }

            })

        }
        mHandler = Handler()
        startRepeat()
        scoreResult.text = "Score - 0"
    }

    private fun startCountDown(duration: Long, interval: Long) {
        object : CountDownTimer(duration, interval) {
            override fun onTick(millisUntilFinished: Long) {
                progressBar.progress = (millisUntilFinished / countDownInterval).toInt()
            }
            override fun onFinish() {
                finish()
            }
        }.start()
    }
    private fun check(index: Int): Boolean {
        val case1 = checkRowFor3(index)
        val case2 = checkColumnFor3(index)
        return case1 || case2
    }

    private fun checkColumnFor3(index: Int): Boolean {
        val chosenBlock = wastes[index].tag
        if (chosenBlock == emptyBlock)
            return false
        val row: Int = index / noOfBlock + 1
        if (row > 2 &&
            wastes[index - noOfBlock].tag as Int == chosenBlock &&
            wastes[index - 2*noOfBlock].tag as Int == chosenBlock
        ) {
            return true
        } else if (
            row in 2..<noOfBlock &&
            wastes[index - noOfBlock].tag as Int == chosenBlock &&
            wastes[index + noOfBlock].tag as Int == chosenBlock
        ) {
            return true
        } else if (
            row < noOfBlock - 1 &&
            wastes[index + noOfBlock].tag as Int == chosenBlock &&
            wastes[index + 2*noOfBlock].tag as Int == chosenBlock
        ) {
            return true
        }
        return false
    }

    private fun checkRowFor3(index: Int): Boolean {
        val chosenBlock = wastes[index].tag
        if (chosenBlock == emptyBlock)
            return false
        val column = index % noOfBlock + 1
        if (
            column > 2 &&
            wastes[index - 1].tag as Int == chosenBlock &&
            wastes[index - 2].tag as Int == chosenBlock
        ) {
            return true
        } else if (
            column in 2..<noOfBlock &&
            wastes[index - 1].tag as Int == chosenBlock &&
            wastes[index + 1].tag as Int == chosenBlock
        ) {
            return true
        } else if (
            column < noOfBlock - 1 &&
            wastes[index + 1].tag as Int == chosenBlock &&
            wastes[index + 2].tag as Int == chosenBlock
        ) {
            return true
        }
        return false
    }

    private fun startRepeat() {
        repeatChecker.run()
    }

    private fun checkRowForThree() {
        val allBlocksExceptLastTwoColumns = (0 until noOfBlock*noOfBlock - 2)
            .filter { it % noOfBlock != noOfBlock - 1 || it % noOfBlock != noOfBlock - 2 }
            .toList()
        for (i in allBlocksExceptLastTwoColumns) {
            val chosenBlock = wastes[i].tag
            val isBlank: Boolean = wastes[i].tag == emptyBlock

            var x = i

            if (wastes[x++].tag as Int == chosenBlock
                && !isBlank
                && wastes[x++].tag as Int == chosenBlock
                && wastes[x].tag as Int == chosenBlock
            ) {
                animation(i + 1)
                score += 3
                scoreResult.text = "Score - $score"
                wastes[x].setImageResource(emptyBlock)
                wastes[x].tag = emptyBlock
                x--
                wastes[x].setImageResource(emptyBlock)
                wastes[x].tag = emptyBlock
                x--
                wastes[x].setImageResource(emptyBlock)
                wastes[x].tag = emptyBlock
            }
        }
        moveBlocksDown()
    }

    private fun checkColumnForThree() {
        val allBlocksExceptLastTwoRows = (0..<noOfBlock*noOfBlock - 2*noOfBlock)
            .toList()
        for (i in allBlocksExceptLastTwoRows) {
            val chosenBlock = wastes[i].tag
            val isBlank: Boolean = wastes[i].tag == emptyBlock
            var x = i
            if (wastes[x].tag as Int == chosenBlock
                && !isBlank
                && wastes[x + noOfBlock].tag as Int == chosenBlock
                && wastes[x + 2*noOfBlock].tag as Int == chosenBlock
            ) {
                animation( i + noOfBlock)
                score += 3
                scoreResult.text = "Score - $score"
                wastes[x].setImageResource(emptyBlock)
                wastes[x].tag = emptyBlock
                x += noOfBlock
                wastes[x].setImageResource(emptyBlock)
                wastes[x].tag = emptyBlock
                x += noOfBlock
                wastes[x].setImageResource(emptyBlock)
                wastes[x].tag = emptyBlock
            }
        }
        moveBlocksDown()
    }

    private fun moveBlocksDown() {
        val firstRow: List<Int> = (0 until noOfBlock).toList()
        for (i in noOfBlock*noOfBlock - noOfBlock - 1 downTo  0) {
            if (wastes[i + noOfBlock].tag as Int == emptyBlock) {
                wastes[i + noOfBlock].setImageResource(wastes[i].tag as Int)
                wastes[i + noOfBlock].tag = wastes[i].tag as Int

                wastes[i].setImageResource(emptyBlock)
                wastes[i].tag = emptyBlock

                // if the first row is blank
                if (firstRow.contains(i) && wastes[i].tag == emptyBlock) {
                    val randomColor: Int = abs(Math.random() * WASTE_TYPE.size).toInt()
                    wastes[i].setImageResource(WASTE_TYPE[randomColor])
                    wastes[i].tag = WASTE_TYPE[randomColor]
                }
            }
        }
        for (i in firstRow) {
            if (wastes[i].tag as Int == emptyBlock) {
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
                moveBlocksDown()
            }
            finally {
                mHandler.postDelayed(this, interval)
            }
        }
    }

    private fun swapBlocks() {
        val background: Int = wastes[blockEnd].tag as Int
        val background1: Int = wastes[blockStart].tag as Int

        wastes[blockStart].setImageResource(background)
        wastes[blockStart].tag = background

        wastes[blockEnd].setImageResource(background1)
        wastes[blockEnd].tag = background1
    }

    private fun randomWaste(imageView: ImageView) {
        var i = 0
        do {
            val random: Int = floor(Math.random() * WASTE_TYPE.size).toInt()
            imageView.setImageResource(WASTE_TYPE[random])
            imageView.tag = WASTE_TYPE[random]
        } while (check((imageView.id + i++) % WASTE_TYPE.size))
    }

    private fun createBoard() {
        gridlayout.rowCount = noOfBlock
        gridlayout.columnCount = noOfBlock
        gridlayout.layoutParams.width = widthOfScreen
        gridlayout.layoutParams.height = heightOfScreen
        for (i in 0 until noOfBlock*noOfBlock) {
            val imageView = ImageView(this)
            imageView.id = i
            val sizeInPixels = convertDpToPixel(50f, this)
            val layoutParams = GridLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams(sizeInPixels, sizeInPixels)
            )
            val margin = 2
            layoutParams.setMargins(margin, margin, margin, margin)
            imageView.layoutParams = android.view.ViewGroup.LayoutParams(widthOfBlock, widthOfBlock)
            imageView.setImageResource(emptyBlock)
            imageView.tag = emptyBlock
            wastes.add(imageView)
            gridlayout.addView(imageView)
        }
        for (i in 0 until noOfBlock*noOfBlock) {
            randomWaste(wastes[i])
        }
    }

    private fun convertDpToPixel(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private fun animation(index: Int) {
        val icon = wastes[index].tag as Int

        val handlerX = Handler(Looper.getMainLooper())
        val handlerY = Handler(Looper.getMainLooper())
        val handlerZ = Handler(Looper.getMainLooper())
        val imageView = ImageView(this)
        val sizeInPixels = convertDpToPixel(50f, this)
        val layoutParams = FrameLayout.LayoutParams(sizeInPixels, sizeInPixels)
        layoutParams.leftMargin = wastes[index].x.toInt()
        layoutParams.topMargin =  wastes[index].y.toInt()

        imageView.setImageResource(icon)
        imageView.layoutParams = layoutParams

        val density = resources.displayMetrics.density
        val elevationInPixels = 2 * density
        imageView.elevation = elevationInPixels
        frameLayout.addView(imageView)

//        val x = imageInorganic.width / 2 - sizeInPixels / 2
        for (i in classification) {
            Log.i("com", i.toString())
        }
        Log.i("com", classification[icon].toString())
        var x: Int = 0;
        when (classification[icon]) {
            1 -> {
                x = (imageInorganic.x + imageInorganic.width / 2 - sizeInPixels / 2).toInt()
            }
            2 -> {
                x = (imageOrganic.x + imageOrganic.width / 2 - sizeInPixels / 2).toInt()
            }
            3 -> {
                x = (imageBiohazard.x + imageBiohazard.width / 2 - sizeInPixels / 2).toInt()
            }
            4 -> {
                x = (imageRecycle.x + imageRecycle.width / 2 - sizeInPixels / 2).toInt()
            }
        }
        val coordinateX = x + layout.x.toInt()
        val runnableX = Runnable {
            layoutParams.leftMargin = coordinateX
            imageView.layoutParams = layoutParams
        }
        handlerX.postDelayed(runnableX, 500)
        val runnableY = Runnable {
            layoutParams.topMargin = imageInorganic.y.toInt() + layout.y.toInt()
            imageView.layoutParams = layoutParams
        }
        handlerY.postDelayed(runnableY, 1000)

        val runnableZ = Runnable {
            frameLayout.removeView(imageView)
        }
        handlerZ.postDelayed(runnableZ, 2000)
    }
}
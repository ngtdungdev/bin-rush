package com.example.bin_rush.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.bin_rush.R
import kotlin.random.Random

class PlayActivity1 : AppCompatActivity() {
    private val WASTE_TYPE = intArrayOf(
        R.drawable.apple,
        R.drawable.apple_core,
        R.drawable.banana,
        R.drawable.banana_super,
        R.drawable.battery_status,
        R.drawable.box,
        R.drawable.bottle,
        R.drawable.bottle_super,
        R.drawable.broken_glass,
        R.drawable.coffee_cup,
        R.drawable.dirty_clothes,
        R.drawable.fish_bone,
        R.drawable.glass_bottle,
        R.drawable.metal,
        R.drawable.metal_super,
        R.drawable.mineral_water,
        R.drawable.plastic,
        R.drawable.plastic_package,
        R.drawable.paper,
        R.drawable.paper_super,
        R.drawable.vaccine,
        R.drawable.vaccine_super,
        R.drawable.waste,
    )
    private val classification = mapOf(
        R.drawable.apple to 2,
        R.drawable.apple_core to 2,
        R.drawable.banana to 2,
        R.drawable.banana_super to 2,
        R.drawable.battery_status to 3,
        R.drawable.box to 4,
        R.drawable.bottle to 4,
        R.drawable.bottle_super to 4,
        R.drawable.coffee_cup to 4,
        R.drawable.broken_glass to 1,
        R.drawable.dirty_clothes to 1,
        R.drawable.fish_bone to 2,
        R.drawable.glass_bottle to 1,
        R.drawable.metal to 4,
        R.drawable.metal_super to 4,
        R.drawable.mineral_water to 1,
        R.drawable.plastic to 1,
        R.drawable.plastic_package to 1,
        R.drawable.paper to 1,
        R.drawable.paper_super to 1,
        R.drawable.vaccine to 3,
        R.drawable.vaccine_super to 3,
        R.drawable.waste to 3,
    )

    private var xDelta = 0
    private var yDelta = 0
    private lateinit var imageInorganic: ImageView
    private lateinit var imageOrganic: ImageView
    private lateinit var imageBiohazard: ImageView
    private lateinit var imageRecycle: ImageView

    private lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play1)
        frameLayout = findViewById(R.id.board)
        imageBiohazard = findViewById(R.id.imageBiohazard)
        imageInorganic = findViewById(R.id.imageInorganic)
        imageOrganic = findViewById(R.id.imageOrganic)
        imageRecycle = findViewById(R.id.imageRecycle)


        frameLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                frameLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val boardWidth = frameLayout.width
                val boardHeight = frameLayout.height

                repeat(60) {
                    addImageView(boardWidth, boardHeight)
                }
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addImageView(boardWidth: Int, boardHeight: Int) {
        val randomWasteType = WASTE_TYPE.random()
        val imageView = ImageView(this@PlayActivity1)
        imageView.setImageResource(randomWasteType)
        imageView.tag = randomWasteType

        val desiredWidth = 100
        val desiredHeight = 100

        val layoutParams = FrameLayout.LayoutParams(
            desiredWidth,
            desiredHeight
        )

        imageView.layoutParams = layoutParams
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        val randomX = Random.nextInt(0, boardWidth - desiredWidth)
        val randomY = Random.nextInt(300, boardHeight - desiredHeight)

        layoutParams.leftMargin = randomX
        layoutParams.topMargin = randomY

        imageView.setOnTouchListener(MyTouchListener())

        frameLayout.addView(imageView, layoutParams)
    }

    private inner class MyTouchListener : View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility", "InflateParams")
        override fun onTouch(view: View, event: MotionEvent): Boolean {
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()

            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    val layoutParams = view.layoutParams as FrameLayout.LayoutParams
                    xDelta = x - layoutParams.leftMargin
                    yDelta = y - layoutParams.topMargin
                }
                MotionEvent.ACTION_MOVE -> {
                    val layoutParams = view.layoutParams as FrameLayout.LayoutParams
                    layoutParams.leftMargin = x - xDelta
                    layoutParams.topMargin = y - yDelta

                    if (layoutParams.leftMargin < 0) {
                        layoutParams.leftMargin = 0
                    } else if (layoutParams.leftMargin > frameLayout.width - view.width) {
                        layoutParams.leftMargin = frameLayout.width - view.width
                    }

                    if (layoutParams.topMargin < 0) {
                        layoutParams.topMargin = 0
                    } else if (layoutParams.topMargin > frameLayout.height - view.height) {
                        layoutParams.topMargin = frameLayout.height - view.height
                    }
                    view.layoutParams = layoutParams
                }
                MotionEvent.ACTION_UP -> {
                    val layoutParams = view.layoutParams as FrameLayout.LayoutParams

                    val screenLocation = IntArray(2)
                    val locationInorganic = IntArray(2)
                    val locationOrganic = IntArray(2)
                    val locationBiohazard = IntArray(2)
                    val locationRecycle = IntArray(2)

                    frameLayout.getLocationOnScreen(screenLocation)
                    imageInorganic.getLocationOnScreen(locationInorganic)
                    imageOrganic.getLocationOnScreen(locationOrganic)
                    imageBiohazard.getLocationOnScreen(locationBiohazard)
                    imageRecycle.getLocationOnScreen(locationRecycle)

                    val dropX = screenLocation[0] + layoutParams.leftMargin
                    val dropY = screenLocation[1] + layoutParams.topMargin
                    val xOrganic = locationOrganic[0]
                    val xBiohazard = locationBiohazard[0]
                    val xRecycle = locationRecycle[0]

                    var gameOver = false
                    val wasteType = view.tag as? Int
                    val classificationOfWaste = classification[wasteType]
                    if (wasteType != null && dropY < 120) {
                        if (dropX >= 0 && dropX < xOrganic/4 && classificationOfWaste == 1)
                            frameLayout.removeView(view)
                        else if (dropX >= xOrganic*2/3 && dropX < xBiohazard - xOrganic*2/3 && classificationOfWaste == 2)
                            frameLayout.removeView(view)
                        else if (dropX >= xBiohazard - xOrganic/4 && dropX < xRecycle - xOrganic*2/3 && classificationOfWaste == 3)
                            frameLayout.removeView(view)
                        else if (dropX >= xRecycle - xOrganic/4 && classificationOfWaste == 4) frameLayout.removeView(view)
                        else {
                            gameOver = true
                            frameLayout.removeView(view)
                        }
                    }

                    if (gameOver) {
                        frameLayout.removeView(view)
                        val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)
                        val dialogIcon = dialogView.findViewById<ImageView>(R.id.dialog_icon)
                        dialogIcon.setImageResource(R.drawable.icon_game)
                        val builder = AlertDialog.Builder(this@PlayActivity1)
                            .setTitle("Game Over")
                            .setMessage("You have lost the game.")
                            .setView(dialogView)
                            .setPositiveButton("Exit") { dialog, which ->
                                finish()
                            }
                        val dialog = builder.create()
                        dialog.setCanceledOnTouchOutside(false)
                        dialog.show()
                    }
                }
            }
            frameLayout.invalidate()
            return true
        }
    }
}
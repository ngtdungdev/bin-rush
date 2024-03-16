package com.example.bin_rush.util

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

abstract class OnSwipeListener(context: Context): View.OnTouchListener {
     var gestureDetector: GestureDetector
    override fun onTouch(p0: View?, motionEvent: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(motionEvent!!);
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        val SWIPE_THRESOLD = 100
        val SWIPE_VELOCITY_THRESOLD = 100

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val result = false
            val yDiff: Float
            val xDiff: Float
            if (e1 != null) {
                yDiff = e2.y - e1.y
                xDiff = e2.x - e1.x
                Log.i("com", e1.toString())
                Log.i("com", e2.toString())
            } else
                return result
            if (abs(xDiff) > abs(yDiff)) {
                if (abs(xDiff) > SWIPE_THRESOLD && abs(velocityX) > SWIPE_VELOCITY_THRESOLD) {
                    if (xDiff > 0)
                        onSwipeRight()
                    else
                        onSwipeLeft()
                }
                return true
            } else {
                if (abs(yDiff) > SWIPE_THRESOLD && abs(velocityY) > SWIPE_VELOCITY_THRESOLD) {
                    if (yDiff > 0)
                        onSwipeBottom()
                    else
                        onSwipeTop()
                    return true
                }
            }
            return result
        }
    }

    abstract fun onSwipeBottom()

    abstract fun onSwipeTop()

    abstract fun onSwipeLeft()

    abstract fun onSwipeRight()

    init {
        gestureDetector = GestureDetector(context,GestureListener())
    }

}
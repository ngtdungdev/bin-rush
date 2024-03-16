package com.example.bin_rush

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

open class OnSwipeListener(context: Context): View.OnTouchListener {
     var gestureDetector: GestureDetector
    override fun onTouch(p0: View?, motionEvent: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(motionEvent!!);
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        val SWIPE_THRESOLD = 100
        val SWIPE_VALOCITY_THRESOLD = 100

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
            var yDiff = 0f
            var xDiff = 0f
            if(e1 != null) {
                 yDiff = e2.y - e1.y
                 xDiff = e2.x - e1.x
            } else return result
            if (Math.abs(xDiff) > Math.abs(yDiff)) {
                if (Math.abs(xDiff) > SWIPE_THRESOLD && Math.abs(velocityY) > SWIPE_VALOCITY_THRESOLD) {
                    if (xDiff > 0) {
                        onSwipeRight()
                    } else onSwipeLeft()
                }
                return true
            } else if (Math.abs(yDiff) > SWIPE_THRESOLD && Math.abs(velocityY) > SWIPE_VALOCITY_THRESOLD) {
                if (yDiff > 0) {
                    onSwipeTop()
                } else onSwipeBottom()
                return true
            }
            return result
        }
    }

    open fun onSwipeBottom() {

    }

    open fun onSwipeTop() {

    }

    open fun onSwipeLeft() {

    }

    open fun onSwipeRight() {

    }

    init {
        gestureDetector = GestureDetector(context,GestureListener())
    }

}
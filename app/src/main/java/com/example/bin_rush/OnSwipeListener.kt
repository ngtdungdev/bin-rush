package com.example.bin_rush

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

open class OnSwipeListener(context: Context): View.OnTouchListener {
    var gestureDetector: GestureDetector
    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(p1);
    }

    inner class GestureListener : GestureListener.SimPle

}
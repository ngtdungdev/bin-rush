package com.example.bin_rush.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.bin_rush.R
import com.example.bin_rush.util.OnFragmentListener
import com.example.bin_rush.util.ProgressListener
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class DateTimeFragment: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.daily_fragment, container, false)
        monday = view.findViewById(R.id.Monday)
        tuesday = view.findViewById(R.id.Tuesday)
        wednesday = view.findViewById(R.id.Wednesday)
        thursday = view.findViewById(R.id.Thursday)
        friday = view.findViewById(R.id.Friday)
        saturday = view.findViewById(R.id.Saturday)
        sunday = view.findViewById(R.id.Sunday)
        return view
    }
    private lateinit var monday: ImageView
    private lateinit var tuesday: ImageView
    private lateinit var wednesday: ImageView
    private lateinit var thursday: ImageView
    private lateinit var friday: ImageView
    private lateinit var saturday: ImageView
    private lateinit var sunday: ImageView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val today = LocalDate.now()
        val dayOfWeek = today.dayOfWeek
        when(dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH).toString()) {
            "Monday" -> {openDay(monday)}
            "Tuesday" -> {openDay(tuesday)}
            "Wednesday" -> {openDay(wednesday)}
            "Thursday" -> {openDay(thursday)}
            "Friday" -> {openDay(friday)}
            "Saturday" -> {openDay(saturday)}
            "Sunday" -> {openDay(sunday)}

        }
    }
    private var listener: ProgressListener? = null
    private fun fragmentClick() {
        listener?.onProgressListener()
    }
    private fun openDay(image: ImageView) {
        
        Glide.with(this)
            .asGif()
            .load(R.drawable.benefit)
            .into(image)
        fragmentClick()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProgressListener) {
            listener = context
        }
    }
    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        if (window != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height =  ViewGroup.LayoutParams.WRAP_CONTENT
            window.setLayout(width, height)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}
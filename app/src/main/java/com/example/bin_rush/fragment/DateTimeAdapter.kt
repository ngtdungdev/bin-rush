package com.example.bin_rush.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.bin_rush.DTO.Game
import com.example.bin_rush.R
import com.example.bin_rush.activity.PlayActivity
import com.example.bin_rush.adapter.GameAdapter
import com.example.bin_rush.adapter.HorizontalSpaceItemDecoration
import com.example.bin_rush.util.OnAdapterListener

class DateTimeAdapter: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
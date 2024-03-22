package com.example.bin_rush.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.bin_rush.activity.PlayActivity
import com.example.bin_rush.activity.PlayActivity1
import com.example.bin_rush.DTO.Game
import com.example.bin_rush.R
import com.example.bin_rush.adapter.GameAdapter
import com.example.bin_rush.adapter.HorizontalSpaceItemDecoration
import com.example.bin_rush.util.OnAdapterListener


class PlayGameFragment : DialogFragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var gameAdapter: GameAdapter
    private lateinit var listGame: ArrayList<Game>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        listGame = ArrayList()
        listGame.add(Game(1, R.drawable.gamecrush))
        listGame.add(Game(2, R.drawable.gameclassify))
        listGame.add(Game(3, R.drawable.gamecoming))
        gameAdapter =  GameAdapter(requireContext(), listGame)
        gameAdapter.setOnAdapterListener(object : OnAdapterListener {
            override fun onItemClick(position: Int) {
                when (position){
                    0 -> {startActivity(Intent(requireContext(), PlayActivity::class.java))}
                    1 -> {startActivity(Intent(requireContext(), PlayActivity1::class.java))}
                }
            }
        })
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        val scale = resources.displayMetrics.density
        val spacingInPixels = (10 * scale + 0.5f).toInt()
        recyclerView.addItemDecoration(HorizontalSpaceItemDecoration(spacingInPixels))
        recyclerView.adapter = gameAdapter
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

package com.example.bin_rush

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView


class PlayGameFragment : DialogFragment(), OnAdapterListener{

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
        listGame.add(Game(1, R.drawable.apple))
        listGame.add(Game(2, R.drawable.banana))
        listGame.add(Game(3, R.drawable.orangecandy))
        gameAdapter =  GameAdapter(requireContext(), listGame)
        recyclerView.adapter = gameAdapter
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        if (window != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height =  ViewGroup.LayoutParams.WRAP_CONTENT
            window.setLayout(width, height)
        }
    }

    override fun onItemClick(position: Int) {
        when (position){
            1 -> {startActivity(Intent(requireContext(), PlayActivity::class.java))}
        }
    }
}

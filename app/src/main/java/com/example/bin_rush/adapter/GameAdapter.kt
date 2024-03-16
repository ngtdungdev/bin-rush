package com.example.bin_rush.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.bin_rush.DTO.Game
import com.example.bin_rush.util.OnAdapterListener
import com.example.bin_rush.R

class GameAdapter(
    private val context: Context,
    private val data: List<Game>,
): RecyclerView.Adapter<GameAdapter.ViewHolder>(){


    private lateinit var listener: OnAdapterListener

    fun setOnAdapterListener(listener: OnAdapterListener) {
        this.listener = listener
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.findViewById(R.id.imageView)
        init {
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_dialog, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = data[position]
        holder.imageView.setBackgroundResource(game.drawable)
    }


    override fun getItemCount(): Int = data.size
}
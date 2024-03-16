package com.example.bin_rush

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GameAdapter(
    private val context: Context,
    private val data: List<Game>,
): RecyclerView.Adapter< GameAdapter.ViewHolder >(){
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    private var listener: OnAdapterListener? = null
     private fun adapterClick(position: Int) {
        listener?.onItemClick(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_dialog, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = data[position]
        holder.imageView.setBackgroundResource(game.drawable)
        holder.imageView.setOnClickListener {
            if (listener != null) {
                adapterClick(position)
            }
        }
    }


    override fun getItemCount(): Int = data.size
}
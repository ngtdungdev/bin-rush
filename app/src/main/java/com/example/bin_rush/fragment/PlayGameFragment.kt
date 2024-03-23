package com.example.bin_rush.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.bin_rush.DTO.Game
import com.example.bin_rush.R
import com.example.bin_rush.activity.MainActivity
import com.example.bin_rush.activity.PlayActivity
import com.example.bin_rush.activity.PlayActivity1
import com.example.bin_rush.adapter.GameAdapter
import com.example.bin_rush.adapter.HorizontalSpaceItemDecoration
import com.example.bin_rush.util.HeartDecreaseListener
import com.example.bin_rush.util.HeartUpdateListener
import com.example.bin_rush.util.OnAdapterListener
import com.example.bin_rush.util.OnFragmentListener

class PlayGameFragment : DialogFragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var gameAdapter: GameAdapter
    private lateinit var listGame: ArrayList<Game>

    private lateinit var imageHeart1: ImageView
    private lateinit var imageHeart2: ImageView
    private lateinit var imageHeart3: ImageView
    private lateinit var imageUnHeart1: ImageView
    private lateinit var imageUnHeart2: ImageView
    private lateinit var imageUnHeart3: ImageView

    private var heartDecreaseListener: HeartDecreaseListener? = null
    private var heartUpdateListener: HeartUpdateListener? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentListener) {
            listener = context
        }
        if (context is HeartDecreaseListener) {
            heartDecreaseListener = context
        } else {
            throw RuntimeException("$context must implement HeartDecreaseListener")
        }
        if (context is HeartUpdateListener) {
            heartUpdateListener = context
        } else {
            throw RuntimeException("$context must implement HeartUpdateListener")
        }
    }

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

        val activity = activity
        if(activity != null){
            imageHeart1 = activity.findViewById<ImageView>(R.id.imageHeart1)
            imageUnHeart1 = activity.findViewById<ImageView>(R.id.imageUnHeart1)
            imageHeart2 = activity.findViewById<ImageView>(R.id.imageHeart2)
            imageUnHeart2 = activity.findViewById<ImageView>(R.id.imageUnHeart2)
            imageHeart3 = activity.findViewById<ImageView>(R.id.imageHeart3)
            imageUnHeart3 = activity.findViewById<ImageView>(R.id.imageUnHeart3)
        }
        var remainingHearts = arguments?.getInt("remainingHearts", 0) ?: 0

        listGame = ArrayList()
        listGame.add(Game(1, R.drawable.gamecrush))
        listGame.add(Game(2, R.drawable.gameclassify))
        listGame.add(Game(3, R.drawable.gamecoming))
        gameAdapter = GameAdapter(requireContext(), listGame)
        gameAdapter.setOnAdapterListener(object : OnAdapterListener {
            override fun onItemClick(position: Int) {
                if (remainingHearts > 0) {
                    when (position) {
                        0 -> {
                            startActivityLauncher.launch(Intent(requireContext(), PlayActivity::class.java))
                            heartDecreaseListener?.decreaseHearts()
                        }
                        1 -> {
                            startActivityLauncher.launch(Intent(requireContext(), PlayActivity1::class.java))
                            heartDecreaseListener?.decreaseHearts()
                        }
                    }
                } else {
                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    alertDialogBuilder.setTitle("Notification")
                    alertDialogBuilder.setMessage("You've run out of plays, please watch an ad!")
                    alertDialogBuilder.setPositiveButton("Watch Ad") { dialog, _ ->
                        remainingHearts++
                        heartUpdateListener?.updateHeartsVisibility()
                        (activity as? MainActivity)?.updateRemainingHearts(remainingHearts)
                        dialog.dismiss()
                    }
                    alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
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

    @RequiresApi(Build.VERSION_CODES.R)
    private val startActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data!!
            val resultData = data.getIntExtra("result",0)
            fragmentClick(resultData)
        }
    }
    private var listener: OnFragmentListener? = null
    private fun fragmentClick(position: Int) {
        listener?.onFragmentListener(position)
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

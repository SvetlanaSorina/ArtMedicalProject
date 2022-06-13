package com.example.artmedicalproject.grid

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.artmedicalproject.PixelGridView
import com.example.artmedicalproject.databinding.FragmentGridBinding
import com.example.artmedicalproject.utils.toPx


class GridFragment : Fragment() {
    private lateinit var binding: FragmentGridBinding
    private val viewModel: GridViewModel by viewModels()
    private val navArgs: GridFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pixelGrid = createPixelGrid()
        binding.root.apply {
            val constraintSet = ConstraintSet()
            constraintSet.clone(this)
            constraintSet.connect(this.id, ConstraintSet.START, pixelGrid.id, ConstraintSet.START)
            constraintSet.connect(this.id, ConstraintSet.TOP, pixelGrid.id, ConstraintSet.TOP)
            constraintSet.connect(this.id, ConstraintSet.END, pixelGrid.id, ConstraintSet.END)
            constraintSet.connect(binding.submitButton.id, ConstraintSet.TOP, pixelGrid.id, ConstraintSet.BOTTOM, 10.toPx())
            setConstraintSet(constraintSet)
            addView(pixelGrid)
        }
    }

    private fun createPixelGrid(): PixelGridView {
        val displayMetrics = requireContext().resources.displayMetrics
        Log.d("ZZZ", "createPixelGrid: ${displayMetrics.heightPixels}")
        val margin = 10.toPx()
        val buttonHeight = 50.toPx()
        //TODO
        val buttonSpace = buttonHeight + (margin * 2) * 3
        Log.d("ZZZ", "createPixelGrid: $margin - $buttonHeight - $buttonSpace ")
        val screenHeight = displayMetrics.heightPixels - buttonSpace

        return PixelGridView(
            screenWidth = displayMetrics.widthPixels,
            screenHeight = screenHeight,
            numColumns = navArgs.columnCount,
            numRows = navArgs.rowCount,
            context = requireContext()
        ).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(this.viewWidth, this.viewHeight).apply {
                horizontalBias = 0.5f
                verticalBias = 0.5f
            }
        }
    }
}
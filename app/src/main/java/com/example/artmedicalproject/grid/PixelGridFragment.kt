package com.example.artmedicalproject.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.artmedicalproject.PixelGridView
import com.example.artmedicalproject.R
import com.example.artmedicalproject.databinding.FragmentPixelGridBinding
import com.example.artmedicalproject.utils.toPx
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PixelGridFragment : Fragment() {
    private lateinit var binding: FragmentPixelGridBinding
    private val viewModel: PixelGridViewModel by activityViewModels()
    private val navArgs: PixelGridFragmentArgs by navArgs()

    private val startTime = System.currentTimeMillis()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPixelGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        if (navArgs.isPainted) {
            observeCellArray()
        } else {
            addPixelGrid(null)
        }
    }

    private fun initViews() {
        binding.islandCount.isVisible = navArgs.isPainted
        binding.submitButton.setOnClickListener {
            if (navArgs.isPainted) {
                navigateToGridSizeFormFragment()
            } else {
                navigateToPainedPixelGridFragment()
            }
        }
    }

    private fun setNumIslands(numIslands: Int) {
        binding.islandCount.text = "Number of islands: ${numIslands}"
    }

    private fun setTime() {
        val endTime = System.currentTimeMillis()
        val time = (endTime - startTime).toInt()
        binding.timeTextView.text = "Time: $time ms"
    }

    private fun observeCellArray() {
        viewLifecycleOwner.lifecycleScope.launch {
            addPixelGrid(viewModel.cellArrayFlow.replayCache.firstOrNull())
        }
    }

    private fun addPixelGrid(cellArray: Array<Array<Triple<Boolean, Int, Boolean>>>?) {
        val displayMetrics = requireContext().resources.displayMetrics
        val footerLayout = 250.toPx()
        val pxWidth = displayMetrics.widthPixels
        val pxHeight = displayMetrics.heightPixels - footerLayout
        val pixelGrid = getPixelGrid(pxWidth, pxHeight, cellArray)
        binding.viewLayout.addView(pixelGrid)

        setTime()
    }

    private fun getPixelGrid(
        pxWidth: Int,
        pxHeight: Int,
        cellArray: Array<Array<Triple<Boolean, Int, Boolean>>>?
    ): PixelGridView {
        return PixelGridView(
            pxWidth = pxWidth,
            pxHeight = pxHeight,
            numColumns = navArgs.columnCount,
            numRows = navArgs.rowCount,
            isPainted = navArgs.isPainted,
            cellArray = cellArray,
            context = requireContext()
        ) { numIslands, cells ->
            setNumIslands(numIslands)
            viewModel.emitCellArray(cells)
        }.apply {
            id = View.generateViewId()
            layoutParams = getLayoutParamsForPixelGridView(this)
        }
    }

    private fun getLayoutParamsForPixelGridView(view: PixelGridView): FrameLayout.LayoutParams {
        return FrameLayout.LayoutParams(view.getViewWidth() + 10, view.getViewHeight() + 10)
    }

    private fun navigateToPainedPixelGridFragment() {
        findNavController().navigate(
            PixelGridFragmentDirections.actionPixelGridFragmentSelf(
                navArgs.columnCount,
                navArgs.rowCount,
                true
            )
        )
    }

    private fun navigateToGridSizeFormFragment() {
        findNavController().navigate(PixelGridFragmentDirections.actionPixelGridFragmentToGridSizeFormFragment())
    }
}
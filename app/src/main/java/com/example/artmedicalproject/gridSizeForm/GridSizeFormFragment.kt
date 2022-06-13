package com.example.artmedicalproject.gridSizeForm

import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.artmedicalproject.databinding.FragmentGridSizeFormBinding
import kotlinx.coroutines.flow.collect

class GridSizeFormFragment : Fragment() {
    private lateinit var binding: FragmentGridSizeFormBinding
    private val viewModel: GridSizeFormViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGridSizeFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeIsButtonEnabled()
    }

    private fun initViews() {
        binding.columnCountInput.apply {
            filters = arrayOf<InputFilter>(InputFilterMinMax("1", "1000"))
            doOnTextChanged { text, _, _, _ ->
                viewModel.emitColumnCount(text.toString())
            }
        }


        binding.rowCountInput.apply {
            filters = arrayOf<InputFilter>(InputFilterMinMax("1", "1000"))
            doOnTextChanged { text, _, _, _ ->
                viewModel.emitRowCount(text.toString())
            }
        }

        binding.submitButton.setOnClickListener {
            findNavController().navigate(GridSizeFormFragmentDirections.actionGridSizeFormFragmentToGridFragment(
                binding.columnCountInput.text?.toString()?.toInt()?: 1,
                binding.rowCountInput.text?.toString()?.toInt()?: 1
            ))
        }
    }

    private fun observeIsButtonEnabled() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.isButtonEnabled.collect {
                binding.submitButton.isEnabled = it
            }
        }

    }

    class InputFilterMinMax(min: String, max: String) : InputFilter {
        private var min: Int
        private var max: Int

        init {
            this.min = min.toInt()
            this.max = max.toInt()
        }

        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            try {
                val input = (dest.toString() + source.toString()).toInt()
                if (isInRange(min, max, input)) return null
            } catch (nfe: NumberFormatException) {
            }
            return ""
        }

        private fun isInRange(a: Int, b: Int, c: Int): Boolean {
            return if (b > a) c in a..b else c in b..a
        }
    }
}
package com.example.artmedicalproject.gridSizeForm

import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.artmedicalproject.databinding.FragmentGridSizeFormBinding
import com.example.artmedicalproject.utils.InputFilterMinMax
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GridSizeFormFragment : Fragment() {
    companion object {
        private const val MIN_VALUE = "1"
        private const val MAX_VALUE = "1000"
    }

    private lateinit var binding: FragmentGridSizeFormBinding
    private val viewModel: GridSizeFormViewModel by viewModels()

    private val inputFilter = arrayOf<InputFilter>(InputFilterMinMax(MIN_VALUE, MAX_VALUE))

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
        binding.apply {
            setUpInput(numColumnsInput) { text -> viewModel.emitColumnCount(text) }
            setUpInput(numRowsInput) { text -> viewModel.emitRowCount(text) }

            submitButton.setOnClickListener {
                navigateToPixelGridFragment()
            }
        }
    }

    private fun observeIsButtonEnabled() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isButtonEnabled.collect {
                binding.submitButton.isEnabled = it
            }
        }
    }

    private fun setUpInput(editText: EditText, onTextChanged: (String) -> Unit) {
        editText.apply {
            filters = inputFilter
            doOnTextChanged { text, _, _, _ ->
                onTextChanged(text.toString())
            }
        }
    }

    private fun navigateToPixelGridFragment() {
        Log.d("ZZZ", "navigateToPixelGridFragment: ")
        findNavController().navigate(
            GridSizeFormFragmentDirections.actionGridSizeFormFragmentToPixelGridFragment(
                binding.numColumnsInput.text?.toString()?.toInt() ?: 1,
                binding.numRowsInput.text?.toString()?.toInt() ?: 1,
                false
            )
        )
    }
}
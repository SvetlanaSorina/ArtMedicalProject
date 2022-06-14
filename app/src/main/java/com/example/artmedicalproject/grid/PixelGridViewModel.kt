package com.example.artmedicalproject.grid

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow

class PixelGridViewModel() : ViewModel() {

    private val _cellArrayFlow: MutableSharedFlow<Array<Array<Triple<Boolean, Int, Boolean>>>> = MutableSharedFlow(replay = 1)
    val cellArrayFlow = _cellArrayFlow

    fun emitCellArray(cellArray: Array<Array<Triple<Boolean, Int, Boolean>>>){
        _cellArrayFlow.tryEmit(cellArray)
    }
}
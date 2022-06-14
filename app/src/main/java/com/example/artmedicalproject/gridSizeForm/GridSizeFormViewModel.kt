package com.example.artmedicalproject.gridSizeForm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

class GridSizeFormViewModel : ViewModel() {

    private val _numColumnsFlow = MutableStateFlow("")
    fun emitColumnCount(columnCount: String) {
        _numColumnsFlow.tryEmit(columnCount)
    }

    private val _numRowsFlow = MutableStateFlow("")
    fun emitRowCount(rowCount: String) {
        _numRowsFlow.tryEmit(rowCount)
    }


    private val _isButtonEnabled: Flow<Boolean> =
        combine(_numColumnsFlow, _numRowsFlow) { numColumns, numRows ->
            return@combine numColumns.isNotEmpty() && numRows.isNotEmpty()
        }
    val isButtonEnabled = _isButtonEnabled
}
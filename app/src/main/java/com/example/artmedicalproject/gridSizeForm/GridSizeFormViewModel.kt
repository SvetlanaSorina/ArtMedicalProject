package com.example.artmedicalproject.gridSizeForm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

class GridSizeFormViewModel() : ViewModel() {

    private val _columnCountFlow = MutableStateFlow("")
    fun emitColumnCount(columnCount: String){
        _columnCountFlow.tryEmit(columnCount)
    }

    private val _rowCountFlow = MutableStateFlow("")
    fun emitRowCount(rowCount: String){
        _rowCountFlow.tryEmit(rowCount)
    }


    private val _isButtonEnabled: Flow<Boolean> =
        combine(_columnCountFlow, _rowCountFlow){ columnCount, rowCount ->
            return@combine !columnCount.isNullOrEmpty() && !rowCount.isNullOrEmpty()
        }
    val isButtonEnabled = _isButtonEnabled
}
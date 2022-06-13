package com.example.artmedicalproject.grid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artmedicalproject.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class GridViewModel() : ViewModel() {

    private val _itemListFlow: MutableSharedFlow<List<Item>> = MutableSharedFlow(replay = 1)
    val itemListFlow = _itemListFlow

    fun generateList(count: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            Log.d("ZZZ", "generateList START: ${System.currentTimeMillis()}")
            val list = mutableListOf<Item>()
            for (i in 0..count) {
                list.add(Item(i))
            }
            Log.d("ZZZ", "generateList END: ${System.currentTimeMillis()}")
            _itemListFlow.emit(list)
        }
    }
}
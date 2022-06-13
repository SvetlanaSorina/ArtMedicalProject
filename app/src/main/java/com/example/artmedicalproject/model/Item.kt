package com.example.artmedicalproject.model

import kotlin.random.Random

data class Item (
    var id: Int = 0,
    val wasSelected: Boolean = Random.nextInt(0, 10) == 0,
    val color: Int? = null,
    val nearbyItems: List<Item> = emptyList()
)
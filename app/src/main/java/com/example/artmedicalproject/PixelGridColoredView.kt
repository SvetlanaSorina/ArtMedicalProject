package com.example.artmedicalproject

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.random.Random

class PixelGridColoredView : View {
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var numColumns: Int = 0
    private var numRows: Int = 0
    private var viewWidth: Int = 0
    private var viewHeight: Int = 0
    private var cellSize: Int = 0
    private var isPainted: Boolean = false
    private lateinit var onComplete: ((Int, Array<Array<Triple<Boolean, Int, Boolean>>>) -> Unit)

    private val blackPaint: Paint = Paint()
    private var cellChecked: Array<Array<Triple<Boolean, Int, Boolean>>> = emptyArray()
    private var islandCount = 0

    constructor(context: Context? = null, attrs: AttributeSet? = null) : super(context, attrs)

    constructor(
        screenWidth: Int,
        screenHeight: Int,
        numColumns: Int,
        numRows: Int,
        isPainted: Boolean = false,
        cellChecked: Array<Array<Triple<Boolean, Int, Boolean>>>? = null,
        context: Context? = null,
        attrs: AttributeSet? = null,
        onComplete: ((Int, Array<Array<Triple<Boolean, Int, Boolean>>>) -> Unit)
    ) : super(context, attrs) {
        this.screenWidth = screenWidth
        this.screenHeight = screenHeight
        this.numColumns = numColumns
        this.numRows = numRows
        this.isPainted = isPainted
        this.cellSize = calculateCellSize()
        this.viewWidth = cellSize * numColumns
        this.viewHeight = cellSize * numRows
        this.cellChecked = cellChecked ?: Array(numColumns) {
            Array(numRows) { Triple(Random.nextInt(0, 10) == 0, 0, false) }
        }
        this.onComplete = onComplete
    }

    init {
        blackPaint.style = Paint.Style.FILL_AND_STROKE
    }

    fun getViewWidth(): Int {
        return viewWidth
    }

    fun getViewHeight(): Int {
        return viewHeight
    }

    fun getCellChecked(): Array<Array<Triple<Boolean, Int, Boolean>>> {
        return cellChecked
    }

    private fun calculateCellSize(): Int {
        if (numColumns == 0) return 0
        val maxViewWidth = screenWidth / numColumns
        val maxViewHeight = screenHeight / numRows
        return if (maxViewWidth <= maxViewHeight) maxViewWidth else maxViewHeight
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
        if (numColumns == 0 || numRows == 0) {
            return
        }
        for (i in 0 until numColumns) {
            for (j in 0 until numRows) {
                if (cellChecked[i][j].first) {
                    if (isPainted && !cellChecked[i][j].third) {
                        val color = generateRandomColor()
                        updateItem(i, j, color)
                        checkNearByCells(i, j, color)
                        islandCount++
                    }

                    val paint = Paint().apply {
                        color = if (isPainted) cellChecked[i][j].second else Color.BLACK
                    }
                    canvas.drawRect(
                        (i * cellSize).toFloat(),
                        (j * cellSize).toFloat(),
                        ((i + 1) * cellSize).toFloat(),
                        ((j + 1) * cellSize).toFloat(),
                        paint
                    )
                }
            }
        }
        for (i in 1 until numColumns) {
            canvas.drawLine(
                (i * cellSize).toFloat(),
                0f,
                (i * cellSize).toFloat(),
                viewHeight.toFloat(), blackPaint
            )
        }
        for (i in 1 until numRows) {
            canvas.drawLine(
                0f,
                (i * cellSize).toFloat(),
                viewWidth.toFloat(),
                (i * cellSize).toFloat(), blackPaint
            )
        }

        Log.d("ZZZ", "onDraw: ")
        onComplete(islandCount, cellChecked)
    }

    private fun checkCell(i: Int, j: Int, color: Int) {
        if (isNotSafe(i, j)) return

        updateItem(i, j, color)
        checkNearByCells(i, j, color)
    }

    private fun isNotSafe(i: Int, j: Int): Boolean {
        return i !in 0 until numColumns || j !in 0 until numRows || !cellChecked[i][j].first || cellChecked[i][j].third
    }

    private fun updateItem(i: Int, j: Int, color: Int) {
        cellChecked[i][j] = Triple(cellChecked[i][j].first, color, true)
    }

    private fun checkNearByCells(i: Int, j: Int, color: Int) {
        checkCell(i + 1, j, color) //RIGHT
        checkCell(i - 1, j, color) //LEFT
        checkCell(i, j + 1, color) //BOTTOM
        checkCell(i, j - 1, color) //TOP
        checkCell(i + 1, j + 1, color) //RIGHT-BOTTOM
        checkCell(i - 1, j - 1, color) //RIGHT-TOP
        checkCell(i + 1, j - 1, color) //RIGHT-TOP
        checkCell(i - 1, j + 1, color) //LEFT-BOTTOM
    }

    private fun generateRandomColor(): Int {
        return Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
    }
}
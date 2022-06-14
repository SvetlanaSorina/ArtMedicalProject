package com.example.artmedicalproject

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class PixelGridView : View {
    private var pxWidth: Int = 0
    private var pxHeight: Int = 0
    private var numColumns: Int = 0
    private var numRows: Int = 0
    private var viewWidth: Int = 0
    private var viewHeight: Int = 0
    private var cellSize: Int = 0
    private var isPainted: Boolean = false
    private lateinit var onComplete: ((Int, Array<Array<Triple<Boolean, Int, Boolean>>>) -> Unit)
    private var cellArray: Array<Array<Triple<Boolean, Int, Boolean>>> = emptyArray() // IsChecked / Color / isVisited
    private var numIslands = 0
    private val blackPaint: Paint = Paint()

    constructor(context: Context? = null, attrs: AttributeSet? = null) : super(context, attrs)

    constructor(
        pxWidth: Int,
        pxHeight: Int,
        numColumns: Int,
        numRows: Int,
        isPainted: Boolean = false,
        cellArray: Array<Array<Triple<Boolean, Int, Boolean>>>? = null,
        context: Context? = null,
        attrs: AttributeSet? = null,
        onComplete: ((Int, Array<Array<Triple<Boolean, Int, Boolean>>>) -> Unit)
    ) : super(context, attrs) {
        this.pxWidth = pxWidth
        this.pxHeight = pxHeight
        this.numColumns = numColumns
        this.numRows = numRows
        this.isPainted = isPainted
        this.cellSize = calculateCellSize()
        this.viewWidth = cellSize * numColumns
        this.viewHeight = cellSize * numRows
        this.cellArray = cellArray ?: Array(numColumns) { Array(numRows) { Triple(Random.nextInt(0, 10) == 0, 0, false) } }
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

    private fun calculateCellSize(): Int {
        if (numColumns == 0) return 0
        val maxViewWidth = pxWidth / numColumns
        val maxViewHeight = pxHeight / numRows
        return if (maxViewWidth <= maxViewHeight) maxViewWidth else maxViewHeight
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
        if (numColumns == 0 || numRows == 0) {
            return
        }
        for (i in 0 until numColumns) {
            for (j in 0 until numRows) {
                val cell = cellArray[i][j]
                if (cell.first) {
                    if (isPainted && !cell.third) {
                        val color = generateRandomColor()
                        updateItem(i, j, color)
                        checkNearByCells(i, j, color)
                        numIslands++
                    }

                    val paint = Paint().apply {
                        color = if (isPainted) cellArray[i][j].second else Color.BLACK
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
        for (i in 0 .. numColumns) {
            canvas.drawLine(
                (i * cellSize).toFloat(),
                0f,
                (i * cellSize).toFloat(),
                viewHeight.toFloat(), blackPaint
            )
        }
        for (i in 0 .. numRows) {
            canvas.drawLine(
                0f,
                (i * cellSize).toFloat(),
                viewWidth.toFloat(),
                (i * cellSize).toFloat(), blackPaint
            )
        }

        onComplete(numIslands, cellArray)
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

    private fun checkCell(i: Int, j: Int, color: Int) {
        if (isNotSafe(i, j)) return

        updateItem(i, j, color)
        checkNearByCells(i, j, color)
    }

    private fun isNotSafe(i: Int, j: Int): Boolean {
        return i !in 0 until numColumns || j !in 0 until numRows || !cellArray[i][j].first || cellArray[i][j].third
    }

    private fun updateItem(i: Int, j: Int, color: Int) {
        cellArray[i][j] = Triple(cellArray[i][j].first, color, true)
    }

    private fun generateRandomColor(): Int {
        return Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
    }
}
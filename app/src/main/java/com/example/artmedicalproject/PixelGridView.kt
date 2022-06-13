package com.example.artmedicalproject

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class PixelGridView(
    private val screenWidth: Int = 0,
    private val screenHeight: Int = 0,
    private val numColumns: Int = 0,
    private val numRows: Int = 0,
    context: Context? = null,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val blackPaint: Paint = Paint()
    private var cellChecked: Array<BooleanArray> = emptyArray()
    private val cellSize: Int
        get() {
            if (numColumns == 0) return 0
            val maxViewWidth = screenWidth / numColumns
            val maxViewHeight = screenHeight / numRows
            return if (maxViewWidth <= maxViewHeight) maxViewWidth else maxViewHeight
        }
    val viewWidth: Int
        get() = cellSize * numColumns
    val viewHeight: Int
        get() = cellSize * numRows

    init {
        blackPaint.style = Paint.Style.FILL_AND_STROKE
        calculateDimensions()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateDimensions()
    }

    private fun calculateDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return
        }
        cellChecked = Array(numColumns) { BooleanArray(numRows) }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
        if (numColumns == 0 || numRows == 0) {
            return
        }
        for (i in 0 until numColumns) {
            for (j in 0 until numRows) {
                val randomBoolean = Random.nextInt(0, 10) == 0
                if (randomBoolean) {
                    canvas.drawRect(
                        (i * cellSize).toFloat(),
                        (j * cellSize).toFloat(),
                        ((i + 1) * cellSize).toFloat(),
                        ((j + 1) * cellSize).toFloat(),
                        blackPaint
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
    }
}
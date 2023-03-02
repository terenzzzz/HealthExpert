package com.example.healthExpert.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CircleView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var rate: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    init {
        circlePaint.color = Color.BLACK
        circlePaint.style = Paint.Style.STROKE
        circlePaint.strokeWidth = 4f

        fillPaint.color = Color.BLUE
        fillPaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = width / 2f - 2f // leave some margin for stroke

        canvas?.drawCircle(centerX, centerY, radius, circlePaint)

        val fillAngle = rate * 180f
        canvas?.drawArc(centerX - radius, centerY - radius,
            centerX + radius, centerY + radius, -90f, fillAngle, true, fillPaint)
    }
}

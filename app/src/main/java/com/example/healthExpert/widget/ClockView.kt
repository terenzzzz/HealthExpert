package com.example.healthExpert.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*

class ClockView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val hourPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val minutePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val secondPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f

    init {
        // Set up the paint for the hour hand
        hourPaint.color = Color.BLACK
        hourPaint.strokeWidth = 20f
        hourPaint.strokeCap = Paint.Cap.ROUND

        // Set up the paint for the minute hand
        minutePaint.color = Color.GRAY
        minutePaint.strokeWidth = 10f
        minutePaint.strokeCap = Paint.Cap.ROUND

        // Set up the paint for the second hand
        secondPaint.color = Color.RED
        secondPaint.strokeWidth = 5f
        secondPaint.strokeCap = Paint.Cap.ROUND
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w.toFloat() / 2
        centerY = h.toFloat() / 2
        radius = (Math.min(w, h) / 2 * 0.8).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the clock face
        canvas.drawCircle(centerX, centerY, radius, hourPaint)

        // Get the current time
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY) % 12
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        // Draw the hour hand
        val hourAngle = (hour + minute / 60f) * 30f
        val hourX = centerX + radius * 0.5 * Math.cos(Math.toRadians(hourAngle.toDouble())).toFloat()
        val hourY = centerY + radius * 0.5 * Math.sin(Math.toRadians(hourAngle.toDouble())).toFloat()
        canvas.drawLine(centerX, centerY, hourX.toFloat(), hourY.toFloat(), hourPaint)

        // Draw the minute hand
        val minuteAngle = minute * 6f
        val minuteX = centerX + radius * 0.7 * Math.cos(Math.toRadians(minuteAngle.toDouble())).toFloat()
        val minuteY = centerY + radius * 0.7 * Math.sin(Math.toRadians(minuteAngle.toDouble())).toFloat()
        canvas.drawLine(centerX, centerY, minuteX.toFloat(), minuteY.toFloat(), minutePaint)

        // Draw the second hand
        val secondAngle = second * 6f
        val secondX = centerX + radius * 0.8 * Math.cos(Math.toRadians(secondAngle.toDouble())).toFloat()
        val secondY = centerY + radius * 0.8 * Math.sin(Math.toRadians(secondAngle.toDouble())).toFloat()
        canvas.drawLine(centerX, centerY, secondX.toFloat(), secondY.toFloat(), secondPaint)

        // Invalidate the view to update the clock every second
        postInvalidateDelayed(1000)
    }
}
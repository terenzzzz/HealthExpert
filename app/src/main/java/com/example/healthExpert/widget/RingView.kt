package com.example.healthExpert.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View


class RingView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var paint: Paint? = null
    private var rectF: RectF? = null
    private val startAngle = 0f
    private var sweepAngle = 60f

    init {
        init()
    }

    private fun init() {
        paint = Paint()
        paint!!.setAntiAlias(true)
        paint!!.setStyle(Paint.Style.STROKE)
        paint!!.setStrokeWidth(20f)
        paint!!.setColor(Color.RED)
        rectF = RectF(50f, 50f, 250f, 250f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rectF?.let { paint?.let { it1 -> canvas.drawArc(it, startAngle, sweepAngle, false, it1) } }
    }

    fun setSweepValue(sweepValue: Float) {
        sweepAngle = sweepValue
        // 这个方法可以刷新UI
        this.invalidate()
    }
}
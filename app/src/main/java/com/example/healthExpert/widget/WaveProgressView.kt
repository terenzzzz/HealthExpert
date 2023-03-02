package com.example.healthExpert.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class WaveProgressView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var progress = 0
    private var waveHeight = 0f
    private var waveWidth = 0f
    private var waveLength = 0f
    private var waveCount = 0
    private var waveOffset = 0f
    private var textSize = 0f

    init {
        wavePaint.color = Color.BLUE
        wavePaint.style = Paint.Style.FILL

        textPaint.color = Color.BLACK
        textPaint.textSize = 32f
        textPaint.textAlign = Paint.Align.CENTER
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        waveWidth = w.toFloat()
        waveLength = w / 2f
        waveHeight = h / 20f
        waveCount = Math.round(w / waveLength + 1.5).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawWave(canvas)
        drawText(canvas)
        if (progress < 100) {
            progress++
            waveOffset += waveLength / 20f
            if (waveOffset > waveLength) {
                waveOffset -= waveLength
            }
            postInvalidateDelayed(50)
        }
    }

    private fun drawWave(canvas: Canvas) {
        val path = Path()
        path.moveTo(0f, height.toFloat())
        for (i in 0 until waveCount) {
            path.quadTo(
                i * waveLength + waveOffset + waveLength / 4f,
                height - waveHeight,
                i * waveLength + waveOffset + waveLength / 2f,
                height.toFloat()
            )
            path.quadTo(
                i * waveLength + waveOffset + waveLength * 3f / 4f,
                height + waveHeight,
                i * waveLength + waveOffset + waveLength,
                height.toFloat()
            )
        }
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(0f, height.toFloat())
        path.close()
        canvas.drawPath(path, wavePaint)
    }

    private fun drawText(canvas: Canvas) {
        val text = "$progress%"
        val xPos = width / 2f
        val yPos = height / 2f - (textPaint.descent() + textPaint.ascent()) / 2f
        canvas.drawText(text, xPos, yPos, textPaint)
    }
}

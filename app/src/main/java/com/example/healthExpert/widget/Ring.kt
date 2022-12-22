package com.example.healthExpert.widget

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

import androidx.annotation.Nullable


class Ring  // 如果不用后面的参数，就不需要重构后面的，直接将其内容写在第一个构造方法就可以，父类会自动执行后面的构造方法
    (context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    View(context, attrs, defStyleAttr, defStyleRes) {
    private var mMeasureHeigth // 控件高度
            = 0
    private var mMeasureWidth // 控件宽度
            = 0

    // 圆形
//    private var mCirclePaint: Paint? = null
    private var mCircleXY //圆心坐标
            = 0f

    private var mCirclePaint: Paint? = null

    // 圆弧
    private var mArcPaint: Paint? = null
    private var mArcRectF //圆弧的外切矩形
            : RectF? = null
    private var mSweepAngle //圆弧的角度
            = 90f
    private var mSweepValue = 0f

    // 单位文字
    private var mUnitPaint: Paint? = null
    private var mShowUnit //文本内容
            : String? = null
    private var mShowUnitSize //文本大小
            = 0f

    // 数值文字
    private var mValuePaint: Paint? = null
    private var mShowValue //文本内容
            : String? = null
    private var mShowValueSize //文本大小
            = 0f
    private var mValue = ""


    private var bgColor = Color.rgb(247, 214, 161)
    private var sweepColor = Color.rgb(0, 0, 0)

    constructor(context: Context?) : this(context, null) {}
    constructor(context: Context?, @Nullable attrs: AttributeSet?) : this(context, attrs, 0) {}
    constructor(
        context: Context?,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) : this(context, attrs, defStyleAttr, 0) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mMeasureWidth = MeasureSpec.getSize(widthMeasureSpec) //获取控件宽度
        mMeasureHeigth = MeasureSpec.getSize(heightMeasureSpec) //获取控件高度
        setMeasuredDimension(mMeasureWidth, mMeasureHeigth)
        initPaint() // 画笔中用到了宽高所以在此初始化画笔
    }

    /**
     * 准备画笔，
     */
    private fun initPaint() {
        val length = Math.min(mMeasureWidth, mMeasureHeigth).toFloat()
        // 圆的代码
        mCircleXY = length / 2 // 确定圆心坐标

        // 弧线，需要 指定其椭圆的外接矩形
        // 矩形
        mArcRectF = RectF(
            (length * 0.1).toFloat(),
            (length * 0.1).toFloat(),
            (length * 0.9).toFloat(),
            (length * 0.9).toFloat()
        )
        mSweepAngle = mSweepValue / 100f * 360f
        mArcPaint = Paint()
        mArcPaint!!.color = sweepColor
        mArcPaint!!.setStrokeWidth((length * 0.1).toFloat()) //圆弧宽度
        mArcPaint!!.setStyle(Paint.Style.STROKE) //圆弧

        // 圆的底色
        mCirclePaint = Paint()

        mCirclePaint!!.color = bgColor
        mCirclePaint!!.setStrokeWidth((length * 0.1).toFloat()) //圆弧宽度
        mCirclePaint!!.setStyle(Paint.Style.STROKE) //圆弧

        // 文字，只需要设置好文字的起始绘制位置即可
        mShowValue = mValue
        mShowValueSize = 60f
        mValuePaint = Paint()
        mValuePaint!!.setTextSize(mShowValueSize)
        mValuePaint!!.setTextAlign(Paint.Align.CENTER)

        // 文字，只需要设置好文字的起始绘制位置即可
        mShowUnitSize = 40f
        mUnitPaint = Paint()
        mUnitPaint!!.setTextSize(mShowUnitSize)
        mUnitPaint!!.setTextAlign(Paint.Align.CENTER)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制圆弧
        mArcRectF?.let {
            mCirclePaint?.let {
                    it2 -> canvas.drawArc(it, 0F, 360F, false, it2)
            }
            mArcPaint?.let {
                    it1 -> canvas.drawArc(it, 90F, mSweepAngle, false, it1)
            }
        }

        // 绘制数值文字
        mShowValue?.let {
            mValuePaint?.let { it1 ->
                canvas.drawText(
                    it,
                    0,
                    mShowValue!!.length,
                    mCircleXY,
                    mCircleXY+20,
                    it1
                )
            }
        }

        // 绘制单位文字
        mShowUnit?.let {
            mUnitPaint?.let { it1 ->
                canvas.drawText(
                    it,
                    0,
                    mShowUnit!!.length,
                    mCircleXY,
                    mCircleXY+mCircleXY/2,
                    it1
                )
            }
        }
    }

    // 让调用者来设置不同的状态值，使弧形弧度变化
    fun setSweepValue(sweepValue: Float) {
        mSweepValue = if (sweepValue != 0f) {
            sweepValue
        } else {
            25f
        }
        // 这个方法可以刷新UI
        this.invalidate()
    }

    fun setValueText(value: String) {
        mValue = value
        // 这个方法可以刷新UI
        this.invalidate()
    }

    fun setBgColor(bgColor: Int) {
        this.bgColor = bgColor
        // 这个方法可以刷新UI
        this.invalidate()
    }

    fun setSweepColor(sweepColor: Int) {
        this.sweepColor = sweepColor
        // 这个方法可以刷新UI
        this.invalidate()
    }

    fun setUnit(unit: String) {
        this.mShowUnit = unit
        // 这个方法可以刷新UI
        this.invalidate()
    }

}
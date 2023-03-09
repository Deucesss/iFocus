package com.rencaihu.design

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.rencaihu.common.ext.dp
import kotlin.math.roundToInt

class AnalogClock @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttr: Int = 0
) : View(context, attributeSet, defaultStyleAttr) {

    private var mRadius: Float = DEFAULT_RADIUS.dp

    private val mMarkerPaint: Paint =
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.BUTT
            strokeWidth = 2.dp
        }

    private val mHandPaint: Paint =
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL_AND_STROKE
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 2.dp
        }

    private var progress = 7293

    private var mTickMarkerLengthLarge = 0f
    private var mTickMarkerLengthMedium = 0f
    private var mTickMarkerLengthSmall = 0f

    private var mHourHandLength = 0f
    private var mMinuteHandLength = 0f
    private var mSecondHandLength = 0f

    @ColorInt
    private var mPaintColor: Int

    init {
        context.obtainStyledAttributes(attributeSet, R.styleable.AnalogClock).apply {
            mPaintColor = getColor(R.styleable.AnalogClock_clock_color, Color.WHITE)
            mRadius = getDimension(R.styleable.AnalogClock_clock_radius, DEFAULT_RADIUS)
            recycle()
        }
        mMarkerPaint.color = mPaintColor
        mHandPaint.color = mPaintColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var desiredWidth = 0
        var desiredHeight = 0

        if (widthMode == MeasureSpec.EXACTLY) {
            desiredWidth = widthSize
        } else {
            desiredWidth = (mRadius * 2 + paddingLeft + paddingRight).roundToInt()
            if (widthMode == MeasureSpec.AT_MOST) {
                desiredWidth = desiredWidth.coerceAtMost(widthSize)
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = heightSize
        } else {
            desiredHeight = (mRadius * 2 + paddingTop + paddingBottom).roundToInt()
            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight = desiredHeight.coerceAtMost(heightSize)
            }
        }

        mRadius = (desiredWidth - paddingStart - paddingEnd)
            .coerceAtMost(desiredHeight - paddingTop - paddingBottom) / 2f
        computeLengths(mRadius)
        setMeasuredDimension(desiredWidth, desiredHeight)
    }

    private fun computeLengths(radius: Float) {
        mTickMarkerLengthLarge = radius * .2f
        mTickMarkerLengthMedium = radius * .15f
        mTickMarkerLengthSmall = radius * .1f

        mHourHandLength = radius * .5f
        mMinuteHandLength = radius * .7f
        mSecondHandLength = radius * .8f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCenterMarker(canvas)
        drawTickMarks(canvas)
        drawHourHand(canvas)
        drawMinuteHand(canvas)
        drawSecondHand(canvas)
    }

    private fun drawCenterMarker(canvas: Canvas) {
        canvas.translate(width / 2f, height / 2f)
        mMarkerPaint.style = Paint.Style.FILL
        canvas.drawCircle(0f, 0f, 2.dp, mMarkerPaint)
        mMarkerPaint.style = Paint.Style.STROKE
    }

    private fun drawTickMarks(canvas: Canvas) {
        canvas.save()
        for (i in 0 until 60) {
            val stopY =
                if (i % 15 == 0) {
                    mMarkerPaint.strokeWidth = STROKE_WIDTH_LARGE
                    mRadius - mTickMarkerLengthLarge
                } else if (i % 5 == 0) {
                    mMarkerPaint.strokeWidth = STROKE_WIDTH_Medium
                    mRadius - mTickMarkerLengthMedium
                } else {
                    mMarkerPaint.strokeWidth = STROKE_WIDTH_Small
                    mRadius - mTickMarkerLengthSmall
                }
            canvas.drawLine(0f, mRadius, 0f, stopY, mMarkerPaint)
            canvas.rotate(DEGREE_PER_TICK)
        }
        canvas.restore()
    }

    private fun drawHourHand(canvas: Canvas) {
        mHandPaint.color = mPaintColor
        with(canvas) {
            save()
            rotate(180f + (progress % (3600 * 12)) * (360 / (3600 * 12f)))
            drawLine(0f, -24f, 0f, mHourHandLength, mHandPaint)
            restore()
        }
    }

    private fun drawMinuteHand(canvas: Canvas) {
        mHandPaint.color = mPaintColor
        with(canvas) {
            save()
            rotate(180f + (progress % 3600) / 10f)
            drawLine(0f, -24f, 0f, mMinuteHandLength, mHandPaint)
            restore()
        }
    }

    private fun drawSecondHand(canvas: Canvas) {
        mHandPaint.color = Color.RED
        with(canvas) {
            save()
            rotate(180f + (progress % 60) * 6f)
            drawLine(0f, -24f, 0f, mSecondHandLength, mHandPaint)
            restore()
        }
    }

    /**
     * @param progress in seconds
     */
    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }

    companion object {
        const val DEFAULT_RADIUS = 200f
        const val STROKE_WIDTH_LARGE = 4f
        const val STROKE_WIDTH_Medium = 3f
        const val STROKE_WIDTH_Small = 2f

        const val DEGREE_PER_TICK = 6f

    }
}
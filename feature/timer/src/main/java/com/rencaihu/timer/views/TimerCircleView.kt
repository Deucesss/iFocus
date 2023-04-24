package com.rencaihu.timer.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.rencaihu.common.ext.TimeUtil
import com.rencaihu.common.ext.dp
import com.rencaihu.common.ext.sp
import com.rencaihu.timer.R
import com.rencaihu.timer.ui.ongoingtimer.Timer1
import timber.log.Timber
import kotlin.math.roundToInt

class TimerCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(
    context,
    attrs
) {

    private val mRemainderColor: Int
    private val mCompletedColor: Int
    private val mStrokeSize: Float

    private var mRadius: Float

    private val mPaint = Paint()
    private val mTextPaint = TextPaint()
    private val mArcRect = RectF()

    private val tomatoDrawable: Drawable by lazy {
        ContextCompat.getDrawable(context, com.rencaihu.design.R.drawable.ic_tomato)!!.mutate()
    }

    private val tomatoSize: Int
        get() = tomatoDrawable.intrinsicWidth

    private var mTimer: Timer1? = null

    init {

        context.obtainStyledAttributes(R.styleable.TimerCircleView).also {
            mRemainderColor = it.getColor(R.styleable.TimerCircleView_timer_circle_remainder_color, context.getColor(
                com.rencaihu.design.R.color.colorPrimaryLight))
            mCompletedColor = it.getColor(R.styleable.TimerCircleView_timer_circle_completed_color, context.getColor(
                com.rencaihu.design.R.color.colorPrimary))
            mStrokeSize = it.getDimension(R.styleable.TimerCircleView_timer_circle_stroke_size, 5.dp)
            mRadius = it.getDimension(R.styleable.TimerCircleView_timer_circle_radius, 100.dp)
            it.recycle()
        }

        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mStrokeSize
        mPaint.strokeCap = Paint.Cap.ROUND

        mTextPaint.isAntiAlias = true
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.color = mCompletedColor
        mTextPaint.textSize = 18.sp
        Timber.d("mTextPaint.textSize = ${18.sp}")
    }

    fun update(timer: Timer1) {
        if (mTimer !== timer) {
            mTimer = timer
            postInvalidateOnAnimation()
        }
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
            desiredWidth = ((mRadius + mStrokeSize) * 2 + paddingStart + paddingEnd).roundToInt()
            if (widthMode == MeasureSpec.AT_MOST) {
                desiredWidth = desiredWidth.coerceAtMost(widthSize)
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = heightSize
        } else {
            desiredHeight = ((mRadius + mStrokeSize) * 2 + paddingTop + paddingBottom).roundToInt()
            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight = desiredHeight.coerceAtMost(heightSize)
            }
        }

        mRadius = minOf(desiredWidth - paddingStart - paddingEnd, desiredHeight - paddingTop - paddingBottom) / 2f - mStrokeSize

        val finalSize = ((mRadius + mStrokeSize) * 2).roundToInt()

        setMeasuredDimension(finalSize + paddingStart + paddingEnd, finalSize + paddingTop + paddingBottom)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mTimer?.let { timer ->
            val xCenter = width / 2f
            val yCenter = height / 2f
            val radius = mRadius

            mPaint.color = mRemainderColor

            mArcRect.top = yCenter - radius
            mArcRect.right = xCenter + radius
            mArcRect.bottom = yCenter + radius
            mArcRect.left = xCenter - radius

            val gapCount = if (timer.laps == 1) 0 else timer.laps
            val degreePerSection = (360f - GAP_DEGREE * gapCount) / timer.laps
            val startAngle = 270f + (GAP_DEGREE / 2) * (if (gapCount == 0) 0 else 1)
            // draw sections
            for (i in 0 until timer.laps) {
                canvas.drawArc(
                    mArcRect,
                    startAngle + (degreePerSection + GAP_DEGREE) * i,
                    degreePerSection,
                    false,
                    mPaint
                )
            }

            // draw progress
            mPaint.color = mCompletedColor
            for (i in 0 until timer.lastLap - 1) {
                canvas.drawArc(
                    mArcRect,
                    startAngle + (degreePerSection + GAP_DEGREE) * i,
                    degreePerSection,
                    false,
                    mPaint
                )
            }

            canvas.drawArc(
                mArcRect,
                startAngle + (degreePerSection + GAP_DEGREE) * (timer.lastLap - 1),
                (1f * timer.elapsedTime / timer.durationPerLap) * degreePerSection,
                false,
                mPaint
            )

            canvas.drawText(TimeUtil.formatTimeInSeconds((timer.remainingTime / 1000).toInt()), xCenter, yCenter, mTextPaint)

            drawLapsRemaining(canvas, timer)

            if (timer.isRunning) {
                postInvalidateOnAnimation()
            }

        } ?: return
    }

    private fun drawLapsRemaining(canvas: Canvas, timer: Timer1) {
        if (timer.laps == 1) return
        val text = "x${timer.laps - timer.lastLap}"
        val textRect = Rect()
        mTextPaint.getTextBounds(text, 0, text.length, textRect)
        val textHeight = textRect.height()
        val textWidth = textRect.width()
        canvas.drawText(text, 0f, 0f, mTextPaint)
        tomatoDrawable.setTint(mTextPaint.color)
        tomatoDrawable.setBounds(
            textWidth / 2 + 10,
            -tomatoSize + (tomatoSize - textHeight) / 2,
            textWidth / 2 + tomatoSize + 10,
            (tomatoSize - textHeight) / 2
        )
        tomatoDrawable.draw(canvas)
    }

    companion object {
        const val GAP_DEGREE = 6f

    }
}
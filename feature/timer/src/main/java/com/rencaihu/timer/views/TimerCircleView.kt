package com.rencaihu.timer.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.rencaihu.common.ext.TimeUtil
import com.rencaihu.timer.R
import com.rencaihu.timer.ui.ongoingtimer.Timer1

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

    private val mPaint = Paint()
    private val mTextPaint = TextPaint()
    private val mArcRect = RectF()

    private var mTimer: Timer1? = null

    init {

        context.obtainStyledAttributes(R.styleable.TimerCircleView).also {
            mRemainderColor = it.getColor(R.styleable.TimerCircleView_timer_circle_remainder_color, context.getColor(
                com.rencaihu.design.R.color.colorPrimaryLight))
            mCompletedColor = it.getColor(R.styleable.TimerCircleView_timer_circle_completed_color, context.getColor(
                com.rencaihu.design.R.color.colorPrimary))
            mStrokeSize = it.getDimension(R.styleable.TimerCircleView_timer_circle_stroke_size, 12f)
            it.recycle()
        }

        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mStrokeSize
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND

        mTextPaint.isAntiAlias = true
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.color = mCompletedColor
        mTextPaint.textSize = 50f
    }

    fun update(timer: Timer1) {
        if (mTimer !== timer) {
            mTimer = timer
            postInvalidateOnAnimation()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mTimer?.let { timer ->
            val xCenter = width / 2f
            val yCenter = height / 2f
            val radius = xCenter.coerceAtMost(yCenter) - mStrokeSize * 2

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

            if (timer.isRunning) {
                postInvalidateOnAnimation()
            }

        } ?: return
    }

    companion object {
        const val GAP_DEGREE = 6f

    }
}
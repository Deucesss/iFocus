package com.rencaihu.design

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.rencaihu.common.ext.TimeUtil
import com.rencaihu.common.ext.sp
import kotlin.math.roundToInt

class CircularClock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultStyleAttrs: Int = 0
) : View(
    context,
    attrs,
    defaultStyleAttrs),
    IClock {
    private val mSectionPaint =
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = STROKE_WIDTH
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }

    private val mProgressPaint =
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = STROKE_WIDTH
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }

    private val mTextPaint =
        TextPaint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            textSize = 18.sp
            textAlign = Paint.Align.CENTER
        }

    private val tomatoDrawable: Drawable by lazy {
        ContextCompat.getDrawable(context, R.drawable.ic_tomato)!!.mutate()
    }

    private val tomatoSize: Int
        get() = tomatoDrawable.intrinsicWidth

    // Style
    private var mRadius: Float = 200f
    private val mInnerRadius get() = mRadius - mStrokeWidth
    private val innerRect: RectF
        get() = RectF(-mInnerRadius, -mInnerRadius, mInnerRadius, mInnerRadius)
    private var mStrokeWidth = 0f

    // Property
    /**
     * in seconds
     */
    private var mProgress = 0
    /**
     * in seconds
     */
    private var mLapDuration = 1800 // in seconds
    private var mLaps = 1
    private var mGapDegree = 6
    private var mCurLap = 0
    private val gaps: Int
        get() = if (mLaps > 1) mLaps else 0
    private val degreePerLap: Float
        get() = (360f - mGapDegree * gaps) / mLaps

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CircularClock).apply {
            mRadius = getDimension(R.styleable.CircularClock_circular_clock_radius, 200f)
            mSectionPaint.color = getColor(R.styleable.CircularClock_circular_clock_color, Color.RED)
            mProgressPaint.color = getColor(R.styleable.CircularClock_circular_clock_progress_color, Color.GREEN)
            mTextPaint.color = getColor(R.styleable.CircularClock_circular_clock_time_color, Color.GRAY)
            mLaps = getInt(R.styleable.CircularClock_circular_clock_laps, 1)
            mGapDegree = getInt(R.styleable.CircularClock_circular_clock_gap, 6)
            mStrokeWidth = getDimension(R.styleable.CircularClock_circular_clock_stroke_width, 4f)
            mSectionPaint.strokeWidth = mStrokeWidth
            mProgressPaint.strokeWidth = mStrokeWidth
            recycle()
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
            desiredWidth = (mRadius * 2 + paddingStart + paddingRight).roundToInt()
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
        val widthWithoutPadding = desiredWidth - paddingStart - paddingEnd
        val heightWithoutPadding = desiredHeight - paddingTop - paddingBottom
        mRadius = widthWithoutPadding.coerceAtMost(heightWithoutPadding) / 2f
        setMeasuredDimension(desiredWidth, desiredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(width / 2f, height / 2f)
        canvas.save()
        canvas.rotate(-90f + mGapDegree / 2f)
        drawSections(canvas)
        drawProgress(canvas)
        canvas.restore()
        drawTimeRemaining(canvas)
        canvas.translate(0f, mTextPaint.fontMetrics.descent - mTextPaint.fontMetrics.ascent + 10)
        drawLapsRemaining(canvas)
    }

    private fun drawSections(canvas: Canvas) {

        for (i in 0 until mLaps) {
            canvas.drawArc(
                innerRect,
                (degreePerLap + mGapDegree) * i,
                degreePerLap,
                false,
                mSectionPaint
            )
        }
    }

    private fun drawProgress(canvas: Canvas) {
        val lapsPassed = mProgress.floorDiv(mLapDuration)
        val currentLapProgress = mProgress % mLapDuration
        for (i in 0 until lapsPassed) {
            canvas.drawArc(
                innerRect,
                (degreePerLap + mGapDegree) * i,
                degreePerLap,
                false,
                mProgressPaint
            )
        }

        canvas.drawArc(
            innerRect,
            (degreePerLap + mGapDegree) * lapsPassed,
            (1f * currentLapProgress / mLapDuration) * degreePerLap,
            false,
            mProgressPaint
        )
    }

    private fun drawTimeRemaining(canvas: Canvas) {
        val text = TimeUtil.formatTimeInSeconds(mCurLap.coerceAtLeast(1) * mLapDuration - mProgress)
        canvas.drawText(text, 0f, 0f, mTextPaint)
    }

    private fun drawLapsRemaining(canvas: Canvas) {
        if (mLaps == 1) return
        val text = "x${mLaps - mCurLap}"
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

    /**
     * in seconds
     */
    override fun setProgress(progress: Int) {
        mProgress = progress
        invalidate()
    }

    override fun setLaps(laps: Int) {
        mLaps = laps
        invalidate()
    }

    override fun setLapDuration(minutes: Int) {
        mLapDuration = minutes * 60
        invalidate()
    }

    override fun setCurrentLap(curLap: Int) {
        mCurLap = curLap
        invalidate()
    }

    companion object {
        const val STROKE_WIDTH = 8f
    }
}
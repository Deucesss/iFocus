package com.rencaihu.design

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.rencaihu.common.ext.dp
import com.rencaihu.common.ext.sp

class NumberCardView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null
) : View(ctx, attrs) {

    private enum class CardState {
        NORMAL,
        FLIPPING
    }

    private var mCardState: CardState = CardState.NORMAL

    private var mNumber: Int = 0
    private var mPreviousNumber: Int = 0

    private val mCamera: Camera by lazy { Camera() }
    private val mMatrix: Matrix by lazy { Matrix() }

    private val mCornerRadius = 12.dp
    private var mRotateX = 0f
    private var mCenterX = 0f

    private val mNumberPaint: TextPaint = TextPaint().apply {
        isAntiAlias = true
        textSize = 100.sp
        textAlign = Paint.Align.CENTER
        color = Color.WHITE
        typeface = ResourcesCompat.getFont(ctx, R.font.fjalla_one)
    }
    private var mTextBaseline = 0f
    private val mTextHeight = with(mNumberPaint.fontMetrics) {descent - ascent + leading}

    private val mBackgroundColor = ContextCompat.getColor(ctx, R.color.colorFlipClockBackground)
    private val mDividerColor = ContextCompat.getColor(ctx, R.color.colorFlipClockDivider)
    private val mBackgroundPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = mBackgroundColor
        strokeWidth = 2.dp
    }

    private val mShouldDrawMidCard: Boolean
        get() = mCardState == CardState.FLIPPING

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var desiredWidth = 0f
        var desiredHeight = 0f

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        if (widthMode == MeasureSpec.EXACTLY) {
            desiredWidth = widthSize.toFloat()
        } else {
            desiredWidth = mNumberPaint.measureText("8") +  + paddingLeft + paddingRight
            if (widthMode == MeasureSpec.AT_MOST) {
                desiredWidth = desiredWidth.coerceAtMost(widthSize.toFloat())
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = heightSize.toFloat()
        } else {
            desiredHeight = mTextHeight + paddingTop + paddingBottom
            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight = desiredHeight.coerceAtMost(heightSize.toFloat())
            }
        }

        val ws = MeasureSpec.makeMeasureSpec(desiredWidth.toInt(), widthMode)
        val hs = MeasureSpec.makeMeasureSpec(desiredHeight.toInt(), heightMode)

        setMeasuredDimension(ws, hs)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mTextBaseline = h - mNumberPaint.fontMetrics.descent
        mCenterX = width / 2f + paddingLeft - paddingRight
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        drawTopCard(canvas)
        drawBotCard(canvas)
        drawMidCard(canvas)
        canvas.drawLine(0f, height / 2f, width.toFloat(), height / 2f, mBackgroundPaint.apply { color = mDividerColor })
        mBackgroundPaint.color = mBackgroundColor
    }

    private fun drawTopCard(canvas: Canvas) {
        canvas.save()
        canvas.clipRect(0f, 0f, width.toFloat(), height.toFloat() / 2f)
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), mCornerRadius, mCornerRadius, mBackgroundPaint)
        canvas.drawText(mNumber.toString(), mCenterX, mTextBaseline, mNumberPaint)
        canvas.restore()
    }

    private fun drawMidCard(canvas: Canvas) {
        if (!mShouldDrawMidCard) return

        with(canvas) {
            save()
            mMatrix.reset()
            mCamera.save()
            mCamera.translate(0F, 0F, 0f)
            mCamera.rotateX(mRotateX)
            mCamera.rotateY(0f)
            mCamera.getMatrix(mMatrix)
            mCamera.restore()

            val scale = resources.displayMetrics.density
            val mValues = FloatArray(9)
            mMatrix.getValues(mValues)
            mValues[6] = mValues[6] / scale
            mValues[7] = mValues[7] / scale
            mMatrix.setValues(mValues)
            mMatrix.preTranslate(-width / 2F, -height / 2F)
            mMatrix.postTranslate(width / 2F, height / 2F)
            concat(mMatrix)

            val rectF = RectF(0f, height/2f, width.toFloat(), height * 1f)

//            drawRect(rectF, mBackgroundPaint)
//            drawRoundRect(rectF, mCornerRadius, mCornerRadius, mBackgroundPaint)

            if (mRotateX >= 90f) {
                val matrix = Matrix()
                matrix.postRotate(180F)
                matrix.postScale(-1F, 1F)
                val num = mPreviousNumber
                val numBmp = textAsBitmap(num.toString())
                val bmp = Bitmap.createBitmap(numBmp, 0, 0, numBmp.width, numBmp.height, matrix, false)
                drawBitmap(bmp, Rect(0, bmp.height/2, bmp.width, bmp.height), rectF, mNumberPaint)
                bmp.recycle()
            } else {
                val num = mNumber
                val numBmp = textAsBitmap(num.toString())
                drawBitmap(numBmp, Rect(0, numBmp.height / 2, numBmp.width, numBmp.height), rectF, mNumberPaint)
                numBmp.recycle()
            }
            restore()
        }
    }

    private fun textAsBitmap(text: String): Bitmap {
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), mCornerRadius, mCornerRadius, mBackgroundPaint)
        canvas.drawText(text, mCenterX, mTextBaseline, mNumberPaint)
        return bmp
    }

    private fun drawBotCard(canvas: Canvas) {
        canvas.save()
        canvas.clipRect(0, height/2, width, height)
        val number = if (mCardState == CardState.FLIPPING) mPreviousNumber else mNumber
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), mCornerRadius, mCornerRadius, mBackgroundPaint)
        canvas.drawText(number.toString(), mCenterX, mTextBaseline, mNumberPaint)
        canvas.restore()
    }

    private fun setNumber0(number: Int) {
        mPreviousNumber = mNumber
        mNumber = number
    }


    private var cardRotateAnimation: ValueAnimator? = null

    fun setNumber(newNum: Int) {
        if (newNum == mNumber) return
        cardRotateAnimation?.cancel()
        cardRotateAnimation = ValueAnimator.ofFloat(180f, 0f)
        with(cardRotateAnimation!!) {
            duration = 800L
            doOnStart {
                mCardState = CardState.FLIPPING
                setNumber0(newNum)
            }
            addUpdateListener {
                mRotateX = it.animatedValue as Float
                postInvalidate()
            }
            doOnEnd {
                mCardState = CardState.NORMAL
                postInvalidateOnAnimation()
            }
            start()
        }
    }
}
package com.rencaihu.design

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.rencaihu.common.ext.TimeUtil.hoursFromMillis
import com.rencaihu.common.ext.TimeUtil.minutesInMillis
import com.rencaihu.common.ext.TimeUtil.secondsInMillis
import com.rencaihu.design.databinding.LayouFlipClockBinding

class FlipClock @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null
): ConstraintLayout(ctx, attrs) {

    private val binding: LayouFlipClockBinding =
        LayouFlipClockBinding.inflate(LayoutInflater.from(ctx), this, true)

    private var mTime: Long = 500 * 1000L

    fun setTime(time: Long) {
        mTime = time
        val hours = hoursFromMillis(mTime)
        val minutes = minutesInMillis(mTime)
        val seconds = secondsInMillis(mTime)
        binding.hours.numTens.setNumber(hours / 10)
        binding.hours.numOnes.setNumber(hours % 10)
        binding.minutes.numTens.setNumber(minutes / 10)
        binding.minutes.numOnes.setNumber(minutes % 10)
        binding.seconds.numTens.setNumber(seconds / 10)
        binding.seconds.numOnes.setNumber(seconds % 10)
    }

}
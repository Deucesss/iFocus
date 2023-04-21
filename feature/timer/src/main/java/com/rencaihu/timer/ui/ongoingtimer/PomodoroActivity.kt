package com.rencaihu.timer.ui.ongoingtimer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.rencaihu.common.ext.parcelable
import com.rencaihu.timer.R

class PomodoroActivity : BaseFocusActivity() {
    override fun getClockLayoutResource(): Int =
        R.layout.layout_pomodoro_clock

    override fun getFocus(savedInstanceState: Bundle?): BaseFocus {
        if (savedInstanceState != null) {
            return savedInstanceState.parcelable<DownFocus>(EXTRA_FOCUS) as BaseFocus
        }

        return intent.parcelable<DownFocus>(EXTRA_FOCUS) ?: throw Error("")
    }
    companion object {
        fun newIntent(ctx: Context, focus: DownFocus): Intent =
            Intent(ctx, PomodoroActivity::class.java).apply {
                putExtra(EXTRA_FOCUS, focus)
            }
    }
}
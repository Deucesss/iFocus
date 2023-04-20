package com.rencaihu.timer.ui.ongoingtimer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.rencaihu.common.ext.parcelable
import com.rencaihu.timer.R

class CountDownActivity: BaseFocusActivity() {

    override fun getClockLayoutResource(): Int =
        R.layout.layout_count_down_clock

    override fun getFocus(savedInstanceState: Bundle?): BaseFocus {
        if (savedInstanceState != null) {
            return savedInstanceState.parcelable<UpFocus>(EXTRA_FOCUS) as BaseFocus
        }
        return intent.parcelable<DownFocus>(EXTRA_FOCUS) ?: throw Error("$intent")
    }

    companion object {
        @JvmStatic
        fun newIntent(ctx: Context, focus: DownFocus): Intent =
            Intent(ctx, CountDownActivity::class.java).apply {
                putExtra(EXTRA_FOCUS, focus)
            }
    }
}
package com.rencaihu.timer.ui.ongoingtimer

import android.os.Bundle
import com.rencaihu.common.ext.parcelable
import com.rencaihu.timer.R

class CountDownFragment: BaseFocusFragment() {
    override fun getClockLayoutResource(): Int =
        R.layout.layout_count_down_clock

    override fun getFocus(savedInstanceState: Bundle?): BaseFocus {
        return savedInstanceState?.parcelable(BaseFocusActivity.EXTRA_FOCUS) ?: arguments?.parcelable(
            BaseFocusActivity.EXTRA_FOCUS
        ) ?: throw Error("$arguments is null")
    }
}
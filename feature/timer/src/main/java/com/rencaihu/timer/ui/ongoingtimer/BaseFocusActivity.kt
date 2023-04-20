package com.rencaihu.timer.ui.ongoingtimer

import android.os.Bundle
import com.rencaihu.common.BaseActivity
import com.rencaihu.timer.databinding.ActivityBaseFocusBinding

abstract class BaseFocusActivity: BaseActivity<ActivityBaseFocusBinding>() {

    override fun getViewBinding(): ActivityBaseFocusBinding =
        ActivityBaseFocusBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewStub.layoutResource = getClockLayoutResource()
        binding.viewStub.inflate()
    }

    abstract fun getClockLayoutResource(): Int
}
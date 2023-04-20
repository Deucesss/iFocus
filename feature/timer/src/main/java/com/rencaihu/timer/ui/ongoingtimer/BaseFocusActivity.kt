package com.rencaihu.timer.ui.ongoingtimer

import android.os.Bundle
import com.rencaihu.common.BaseActivity
import com.rencaihu.timer.databinding.ActivityBaseFocusBinding

abstract class BaseFocusActivity: BaseActivity<ActivityBaseFocusBinding>() {

    private lateinit var focus: BaseFocus

    abstract fun getClockLayoutResource(): Int

    abstract fun getFocus(savedInstanceState: Bundle?): BaseFocus

    override fun getViewBinding(): ActivityBaseFocusBinding =
        ActivityBaseFocusBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewStub.layoutResource = getClockLayoutResource()
        binding.viewStub.inflate()
        focus = getFocus(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        focus.start()
    }

    override fun onResume() {
        super.onResume()
        focus.recalibrateProgressAndResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        focus.recordTimeUponBackground()
    }

    override fun onDestroy() {
        super.onDestroy()
        focus.stop()
    }

    private fun setListeners() {
    }

    companion object {
        const val EXTRA_FOCUS = "focus"
    }
}
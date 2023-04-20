package com.rencaihu.timer.ui.ongoingtimer

import android.os.Bundle
import com.rencaihu.common.BaseActivity
import com.rencaihu.design.IClock
import com.rencaihu.timer.databinding.ActivityBaseFocusBinding
import timber.log.Timber

abstract class BaseFocusActivity: BaseActivity<ActivityBaseFocusBinding>() {

    protected lateinit var focus: BaseFocus
    private lateinit var clock: IClock

    abstract fun getClockLayoutResource(): Int

    abstract fun getFocus(savedInstanceState: Bundle?): BaseFocus

    override fun getViewBinding(): ActivityBaseFocusBinding =
        ActivityBaseFocusBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewStub.layoutResource = getClockLayoutResource()
        clock = binding.viewStub.inflate() as IClock

        focus = getFocus(savedInstanceState)
        initFocus()
        setListeners()
    }
    private fun initFocus() {
        // TODO: add getters to BaseFocus...
        (focus as? DownFocus)?.let {
            clock.setLapDuration(it.lapDuration)
            clock.setLaps(it.laps)
        }
        clock.setProgress(focus.progress)
        focus.setOnTickListener {
            Timber.d("progress: ${focus.progress}")
            clock.setProgress(++focus.progress)
        }
        focus.setOnFinishListener {

        }

        Timber.d("${focus.status}")
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

    private fun setListeners() {
        binding.btnPause.setOnClickListener {
            binding.switcher.showNext()
            focus.pause()
        }

        binding.btnResume.setOnClickListener {
            binding.switcher.showNext()
            focus.resume()
        }

        binding.btnStop.setOnClickListener {
            focus.stop()
            finish()
        }
    }

    companion object {
        const val EXTRA_FOCUS = "focus"
    }
}
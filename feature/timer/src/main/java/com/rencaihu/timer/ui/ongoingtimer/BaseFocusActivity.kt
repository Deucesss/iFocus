package com.rencaihu.timer.ui.ongoingtimer

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.rencaihu.common.BaseActivity
import com.rencaihu.design.IClock
import com.rencaihu.timer.R
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
        binding.viewStub.inflatedId = R.id.clock
        binding.viewStub.layoutResource = getClockLayoutResource()
        clock =
            (binding.viewStub.inflate().apply {
                // constraint controls to newly inflated clock
                binding.controls.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    topToBottom = R.id.clock
                    bottomToTop = binding.settings.id
                }
            }) as IClock

        focus = getFocus(savedInstanceState)
        initFocus()
        setListeners()
    }
    private fun initFocus() {
        Timber.d("${focus.status}")
        clock.setProgress(focus.progress)
        clock.setLapDuration(focus.lapDuration)
        clock.setLaps(focus.laps)
        focus.setOnTickListener {
            Timber.d("progress: ${focus.progress}")
            clock.setProgress(++focus.progress)
        }
        focus.setOnFinishListener {

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_FOCUS, focus)
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
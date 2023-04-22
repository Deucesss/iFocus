package com.rencaihu.timer.ui.ongoingtimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.rencaihu.common.BaseFragment
import com.rencaihu.design.IClock
import com.rencaihu.timer.EXTRA_FOCUS
import com.rencaihu.timer.R
import com.rencaihu.timer.databinding.ActivityBaseFocusBinding
import timber.log.Timber

abstract class BaseFocusFragment: BaseFragment<ActivityBaseFocusBinding>() {

    private lateinit var focus: BaseFocus
    private lateinit var clock: IClock
    private var timer: Timer? = null

    abstract fun getClockLayoutResource(): Int
    abstract fun getFocus(savedInstanceState: Bundle?): BaseFocus

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityBaseFocusBinding =
        ActivityBaseFocusBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        focus = getFocus(savedInstanceState)
        focus.recalibrateTime()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        inflateClock()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFocus()
        setListeners()
        initTimer()
    }

    private fun inflateClock() {
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
    }

    private fun initFocus() {
        Timber.d("$focus")
        clock.setProgress(focus.progress)
        clock.setLapDuration(focus.lapDuration)
        clock.setLaps(focus.laps)
        clock.setCurrentLap(focus.curLap)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        timer?.cancel()
        focus.saveTimestamp()
        outState.putParcelable(EXTRA_FOCUS, focus)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        // save focus to datastore
    }

    private fun setListeners() {
        binding.btnPause.setOnClickListener {
            binding.switcher.showNext()
            pauseTimer()
        }

        binding.btnResume.setOnClickListener {
            binding.switcher.showNext()
            resumeTimer()
        }

        binding.btnStop.setOnClickListener {
        }
    }

    private fun initTimer() {
        when (focus.status) {
            STATUS.READY -> {
                // start a new lap
                focus.status = STATUS.RUNNING
                focus.nextLap()
                clock.setCurrentLap(focus.curLap)
                newTimer()
                timer?.start()
            }
            STATUS.COMPLETED -> {
                // redirect to completed fragment
            }
            STATUS.PAUSED -> {
                // change UI to paused
                binding.switcher.displayedChild = 1
            }
            STATUS.RUNNING -> {
                // app was killed while focus is running, recalibrate time passed.
                if (focus.timestampOnDestroy != null) {

                }
                // resume timer
                newTimer()
                timer?.start()
            }
        }
    }

    private fun pauseTimer() {
        timer?.cancel()
        focus.status = STATUS.PAUSED
    }

    private fun resumeTimer() {
        newTimer()
        timer?.start()
        focus.status = STATUS.RUNNING
    }

    private fun stopTimer() {
        // stop timer and show dialog
        // resume timer if stop is cancelled
    }

    private fun recordTimeOnEnteringBackground() {
        if (focus.status == STATUS.RUNNING) {
            focus.timestampOnDestroy = System.currentTimeMillis()
        }
    }

    private fun newTimer() {
        timer?.cancel()
        timer = Timer(focus.timeRemaining * 1000L).apply {
            setOnTickListener {
                // increment progress
                clock.setProgress(++focus.progress)
                Timber.d("onTick: $focus")
                // update clock
            }
            setOnFinishListener {  }
        }
    }
}
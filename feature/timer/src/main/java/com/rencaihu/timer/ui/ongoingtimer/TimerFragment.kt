package com.rencaihu.timer.ui.ongoingtimer

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rencaihu.common.BaseFragment
import com.rencaihu.timer.R
import com.rencaihu.timer.databinding.ActivityBaseFocusBinding
import com.rencaihu.timer.databinding.TimerViewBinding

class TimerFragment: BaseFragment<ActivityBaseFocusBinding>() {

    private lateinit var timerBinding: TimerViewBinding

    var mTimerId: Int = 0
        private set

    private val mTimer: Timer1?
        get() = TimerManager.timerManager.getTimer(mTimerId)

    private val mTimerRunnable = TimerRunnable()

    private val mTimerWatcher: TimerWatcher = TimerWatcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTimerId = savedInstanceState?.getInt(EXTRA_TIMER) ?: arguments?.getInt(EXTRA_TIMER) ?: throw Error("No timer passed")
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityBaseFocusBinding =
        ActivityBaseFocusBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // inflate clock
        binding.viewStub.layoutResource = R.layout.timer_view
        timerBinding = TimerViewBinding.bind(binding.viewStub.inflate())
        setListeners()
        TimerManager.timerManager.addTimerListener(mTimerWatcher)
        update(mTimer!!)
    }

    override fun onStart() {
        super.onStart()
        startUpdatingTimer()
    }

    override fun onStop() {
        super.onStop()
        stopUpdatingTimer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        TimerManager.timerManager.removeTimerListener(mTimerWatcher)
    }

    private fun setListeners() {
        binding.btnPause.setOnClickListener {
            TimerManager.timerManager.pauseTimer(mTimer!!.pause())
        }
        binding.btnResume.setOnClickListener {
            TimerManager.timerManager.startTimer(mTimer!!.start())
        }
    }

    private fun startUpdatingTimer() {
        stopUpdatingTimer()
        timerBinding.root.post(mTimerRunnable)
    }

    private fun stopUpdatingTimer() {
        timerBinding.root.removeCallbacks(mTimerRunnable)
    }

    fun update(timer: Timer1) {
        when (timer.state) {
            Timer1.State.READY -> TimerManager.timerManager.startTimer(timer)
            Timer1.State.RUNNING -> binding.switcher.displayedChild = 0
            Timer1.State.PAUSED -> binding.switcher.displayedChild = 1
            Timer1.State.EXPIRED -> {
                // TODO: navigation
                if (timer.lastLap == timer.laps) {

                } else {

                }
            }
        }
        timerBinding.root.update(timer)
    }

    private inner class TimerRunnable: Runnable {
        override fun run() {
            val startTime = SystemClock.elapsedRealtime()
            timerBinding.root.update(mTimer!!)
            val endTime = SystemClock.elapsedRealtime()
            val delay = endTime + 20 - startTime
            timerBinding.root.postDelayed(this, delay)
        }
    }

    private inner class TimerWatcher: TimerListener {
        override fun timerAdded(timer: Timer1) {
        }

        override fun timerUpdated(timer: Timer1) {
            update(timer)
        }

        override fun timerRemoved(timer: Timer1) {
        }
    }

    companion object {
        const val EXTRA_TIMER = "timer"
    }
}
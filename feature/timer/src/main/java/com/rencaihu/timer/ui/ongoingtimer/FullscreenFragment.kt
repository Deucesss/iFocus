package com.rencaihu.timer.ui.ongoingtimer

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.rencaihu.common.BaseFragment
import com.rencaihu.timer.R
import com.rencaihu.timer.data.TimerManager
import com.rencaihu.timer.databinding.FragmentFullscreenBinding

class FullscreenFragment: BaseFragment<FragmentFullscreenBinding>() {

    private var timerUpdateRunnable: TimerUpdateRunnable = TimerUpdateRunnable()

    private val mTimer: Timer?
        get() = TimerManager.timerManager.getTimer()

    private val timerWatcher: TimerListener = TimerWatcher()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFullscreenBinding =
        FragmentFullscreenBinding.inflate(layoutInflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        TimerManager.timerManager.removeTimerListener(timerWatcher)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        TimerManager.timerManager.addTimerListener(timerWatcher)
        update(mTimer!!)
    }

    override fun onStart() {
        super.onStart()
        startUpdating()
    }

    override fun onStop() {
        super.onStop()
        stopUpdating()
    }

    private fun startUpdating() {
        stopUpdating()
        binding.root.post(timerUpdateRunnable)
    }

    private fun stopUpdating() {
        binding.root.removeCallbacks(timerUpdateRunnable)
    }

    private fun setListeners() {
        binding.btnStop.setOnClickListener {
            showCancelTimerDialog()
        }

        binding.btnPause.setOnClickListener {
            TimerManager.timerManager.pauseTimer(mTimer!!.pause())
        }

        binding.btnResume.setOnClickListener {
            TimerManager.timerManager.startTimer(mTimer!!.start())
        }
    }

    private fun showCancelTimerDialog() {
        findNavController().navigate(R.id.action_fullscreenFragment_to_cancelTimerDialogFragment)
    }

    fun update(timer: Timer) {
        when (timer.state) {
            Timer.State.READY -> TimerManager.timerManager.startTimer(timer)
            Timer.State.RUNNING -> binding.switcher.displayedChild = 0
            Timer.State.PAUSED -> binding.switcher.displayedChild = 1
            Timer.State.EXPIRED -> {
                // TODO: navigation
                if (timer.isBreakTimer) {
                    //navigate to break complete
                    TimerManager.timerManager.removeTimerFromDatastore()
                    return
                }
                if (timer.lastLap == timer.laps) {
                    // navigate to complete
                } else {
                    // navigate to break selection
                }
            }
        }
    }

    private inner class TimerUpdateRunnable: Runnable {
        override fun run() {
            val start = System.currentTimeMillis()
            binding.flipClock.setTime(TimerManager.timerManager.getTimer()!!.remainingTime)
            val end = System.currentTimeMillis()
            binding.root.postDelayed(this, end + 20 - start)
        }
    }

    private inner class TimerWatcher: TimerListener {
        override fun timerAdded(timer: Timer) {
        }

        override fun timerUpdated(timer: Timer) {
            update(timer)
        }

        override fun timerRemoved(timer: Timer) {
        }
    }

}
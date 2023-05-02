package com.rencaihu.timer.ui.ongoingtimer

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.rencaihu.common.BaseFragment
import com.rencaihu.timer.R
import com.rencaihu.timer.data.TimerManager
import com.rencaihu.timer.databinding.FragmentTimerBinding

class TimerFragment: BaseFragment<FragmentTimerBinding>() {

    var mTimerId: Int = 0
        private set

    private val mTimer: Timer?
        get() = TimerManager.timerManager.getTimer()

    private val mTimerRunnable = TimerRunnable()

    private val mTimerWatcher: TimerWatcher = TimerWatcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTimerId = savedInstanceState?.getInt(EXTRA_TIMER) ?: arguments?.getInt(EXTRA_TIMER) ?: throw Error("No timer passed")
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTimerBinding =
        FragmentTimerBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // inflate clock
        setListeners()
        TimerManager.timerManager.addTimerListener(mTimerWatcher)
        update(mTimer!!)
    }

    override fun onStart() {
        super.onStart()
        startUpdatingTimer()
    }

    override fun onResume() {
        super.onResume()
        update(mTimer!!)
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
        binding.timerView.root.post(mTimerRunnable)
    }

    private fun stopUpdatingTimer() {
        binding.timerView.root.removeCallbacks(mTimerRunnable)
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
                    Toast.makeText(requireContext(), "Break complete", Toast.LENGTH_SHORT).show()
                    return
                }
                if (timer.lastLap == timer.laps) {
                    // navigate to complete
                    findNavController().navigate(R.id.action_timerFragment_to_focusCompleteFragment)
                } else {
                    // navigate to break selection
                    findNavController().navigate(R.id.action_timerFragment_to_pomodoroBreakFragment)
                }
            }
        }
        binding.timerView.root.update(timer)
    }

    private inner class TimerRunnable: Runnable {
        override fun run() {
            val startTime = SystemClock.elapsedRealtime()
            binding.timerView.root.update(mTimer!!)
            val endTime = SystemClock.elapsedRealtime()
            val delay = endTime + 20 - startTime
            binding.timerView.root.postDelayed(this, delay)
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

    companion object {
        const val EXTRA_TIMER = "timer"
    }
}
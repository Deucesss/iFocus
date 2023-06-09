package com.rencaihu.timer.ui.ongoingtimer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.rencaihu.common.BaseFragment
import com.rencaihu.timer.R
import com.rencaihu.timer.data.TimerManager
import com.rencaihu.timer.databinding.FragmentPomodoroBreakBinding

class PomodoroBreakFragment: BaseFragment<FragmentPomodoroBreakBinding>() {

    private val mTimer: Timer?
        get() = TimerManager.timerManager.getTimer()


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPomodoroBreakBinding =
        FragmentPomodoroBreakBinding.inflate(inflater, container, false)


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDesc.text = "Congrats!\nYou have completed ${mTimer!!.lastLap} pomodoro${if (mTimer!!.lastLap > 1) "s" else ""}!\nTake a break!"
        setListeners()
    }

    private fun setListeners() {
        binding.btnSkip.setOnClickListener {
            TimerManager.timerManager.nextLap(mTimer!!)
            findNavController().navigate(R.id.action_pomodoroBreakFragment_to_timerFragment)
        }

        binding.btnBreak.setOnClickListener {
            TimerManager.timerManager.breakTimer(5000L)
            findNavController().navigate(R.id.action_pomodoroBreakFragment_to_timerFragment)
        }
    }
}
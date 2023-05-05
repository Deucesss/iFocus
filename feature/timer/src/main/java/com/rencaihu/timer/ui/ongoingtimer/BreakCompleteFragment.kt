package com.rencaihu.timer.ui.ongoingtimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.rencaihu.common.BaseFragment
import com.rencaihu.timer.R
import com.rencaihu.timer.data.TimerManager
import com.rencaihu.timer.databinding.FragmentPomodoroBreakBinding

class BreakCompleteFragment: BaseFragment<FragmentPomodoroBreakBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPomodoroBreakBinding =
        FragmentPomodoroBreakBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDesc.text = "Break Complete!\n Continue to next pomodoro?"
        binding.btnBreak.text = "Complete"
        binding.btnSkip.text = "GO!"
        setListeners()
    }

    private fun setListeners() {
        binding.btnSkip.setOnClickListener {
            TimerManager.timerManager.nextLap(TimerManager.timerManager.getTimer()!!)
            findNavController().navigate(R.id.action_breakCompleteFragment_to_timerFragment)
        }

        binding.btnBreak.setOnClickListener {
            findNavController().navigate(R.id.action_breakCompleteFragment_to_focusCompleteFragment)
        }
    }
}
package com.rencaihu.timer.ui.timersetup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.rencaihu.common.BaseFragment
import com.rencaihu.common.ext.repeatOn
import com.rencaihu.timer.R
import com.rencaihu.timer.databinding.LayoutTimerSetupBinding
import com.rencaihu.timer.ui.ongoingtimer.TimerFragment.Companion.EXTRA_TIMER
import com.rencaihu.timer.ui.ongoingtimer.TimerManager

class CountDownSetupFragment: BaseFragment<LayoutTimerSetupBinding>() {

    private val viewModel: TimerSetupViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LayoutTimerSetupBinding =
        LayoutTimerSetupBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.clock.setOnClickListener {
            viewModel.switchDisplayMode()
        }
        binding.switcher.setOnClickListener {
            if (binding.switcher.displayedChild == 1) {
                viewModel.switchDisplayMode()
            }
        }
        binding.wheelNumber.setOnNumberSelectedListener { _, item ->
            viewModel.setDuration(item.toInt())
        }
        binding.btnStart.setOnClickListener {
            TimerManager.timerManager.newTimer(viewModel.uiState.value.timer.durationPerLap, viewModel.uiState.value.timer.laps)
            findNavController().navigate(
                R.id.action_dest_home_1_to_focusActivity,
                bundleOf(EXTRA_TIMER to -1)
            )
        }
    }

    private fun setupObservers() {
        repeatOn(Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                binding.wheelNumber.setDefaultValue((it.timer.durationPerLap / (60  * 1000)).toString())
                binding.switcher.displayedChild = it.displayMode.viewPosition
                binding.clock.update(it.timer)
            }
        }
    }
}
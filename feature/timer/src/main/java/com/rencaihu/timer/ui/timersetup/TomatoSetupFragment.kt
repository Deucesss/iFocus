package com.rencaihu.timer.ui.timersetup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rencaihu.common.BaseFragment
import com.rencaihu.timer.databinding.LayoutTimerSetupBinding
import com.rencaihu.timer.ui.ongoingtimer.DownFocus
import com.rencaihu.timer.ui.ongoingtimer.PomodoroActivity
import kotlinx.coroutines.launch
import timber.log.Timber

class TomatoSetupFragment: BaseFragment<LayoutTimerSetupBinding>() {
    private val viewModel: TimerSetupViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LayoutTimerSetupBinding =
        LayoutTimerSetupBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListeners()
        setupObservers()
    }

    private fun setupView() {
        // default be three laps for tomato
        viewModel.setLaps(3)
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
            startActivity(
                PomodoroActivity.newIntent(
                    requireContext(),
                    DownFocus.newInstance(
                        id = 0L,
                        name = "",
                        lapDuration = viewModel.uiState.value.duration,
                        laps = viewModel.uiState.value.laps
                    )
                )
            )
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    Timber.d("uiState: $it")
                    binding.switcher.displayedChild = it.displayMode.viewPosition
                    binding.clock.setLapDuration(it.duration)
                    binding.clock.setLaps(it.laps)
                    binding.wheelNumber.setDefaultValue(it.duration.toString())
                }
            }
        }
    }
}
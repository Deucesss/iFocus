package com.rencaihu.timer.ui.timersetup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rencaihu.common.BaseFragment
import com.rencaihu.timer.databinding.LayoutTimerSetupBinding
import com.rencaihu.timer.viewmodel.TimerSetupViewModel
import kotlinx.coroutines.launch

class CountDownSetupFragment: BaseFragment<LayoutTimerSetupBinding>() {

    private val viewModel: TimerSetupViewModel by viewModels()

    override fun getViewBinding(): LayoutTimerSetupBinding =
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
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    binding.switcher.displayedChild = it.displayMode.viewPosition
                    binding.clock.setLapDuration(it.duration)
                    binding.wheelNumber.setDefaultValue(it.duration.toString())
                }
            }
        }
    }
}
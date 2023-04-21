package com.rencaihu.timer.ui.timersetup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.rencaihu.common.BaseFragment
import com.rencaihu.timer.R
import com.rencaihu.timer.databinding.LayoutTimerSetupBinding
import com.rencaihu.timer.ui.ongoingtimer.BaseFocusActivity.Companion.EXTRA_FOCUS
import com.rencaihu.timer.ui.ongoingtimer.DownFocus
import kotlinx.coroutines.launch

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
            findNavController().navigate(
                R.id.action_chooseTimerFragment2_to_focusActivity,
                bundleOf(
                    EXTRA_FOCUS to DownFocus.newInstance(
                        0,
                        "",
                        viewModel.uiState.value.duration,
                        1
                    )
                )
            )
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
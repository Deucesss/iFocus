package com.rencaihu.timer.ui.ongoingtimer

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rencaihu.common.BaseActivity
import com.rencaihu.timer.databinding.ActivityPomodoroBinding
import kotlinx.coroutines.launch

class PomodoroActivity : BaseActivity<ActivityPomodoroBinding>() {

    private val viewModel: TimerViewModel by viewModels()

    private lateinit var focus: Focus

    override fun getViewBinding(): ActivityPomodoroBinding =
        ActivityPomodoroBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initParams(savedInstanceState)
        setupListeners()
        setupObservers()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("focus", focus)
    }

    override fun onResume() {
        super.onResume()
        focus.recalibrateTimeOnResume()
        focus.start()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        focus.pause()
        focus.recalibrateTimeOnStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun initParams(savedInstanceState: Bundle?) {
        focus =
            savedInstanceState?.getParcelable("focus") ?:
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("focus", Focus::class.java)
            } else {
                intent.getParcelableExtra("focus")
            } ?: return
        binding.clock.setLaps(focus.laps)
        binding.clock.setLapDuration(focus.lapDuration)
        binding.clock.setProgress(focus.progress)
        focus.setOnTickListener {
            binding.clock.setProgress(focus.progress)
        }
        focus.setOnCompleteListener {  }
    }

    private fun setupListeners() {
        binding.btnPause.setOnClickListener {
            binding.switcher.showNext()
        }

        binding.btnResume.setOnClickListener {
            binding.switcher.showNext()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

            }
        }
    }

    companion object {
        @JvmStatic
        fun newIntent(ctx: Context, focus: Focus): Intent =
            Intent(ctx, PomodoroActivity::class.java).apply {
                putExtra("focus", focus)
            }
    }
}
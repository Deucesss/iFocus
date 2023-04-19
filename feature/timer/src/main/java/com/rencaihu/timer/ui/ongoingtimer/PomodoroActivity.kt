package com.rencaihu.timer.ui.ongoingtimer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import com.rencaihu.common.BaseActivity
import com.rencaihu.timer.databinding.ActivityPomodoroBinding

class PomodoroActivity : BaseActivity<ActivityPomodoroBinding>() {

    private lateinit var timer: Timer

    private var lapDuration: Int = 0
    private var lap: Int = 3

    override fun getViewBinding(): ActivityPomodoroBinding =
        ActivityPomodoroBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnPause.setOnClickListener {
            binding.switcher.showNext()
        }

        binding.btnResume.setOnClickListener {
            binding.switcher.showNext()
        }
    }

    /**
     * @param duration Time in minutes
     */
    class Timer private constructor(private val duration: Long) {

        private var timer: CountDownTimer? = null

        fun start() {
            timer = object : CountDownTimer(duration, COUNT_DOWN_INTERVAL) {
                override fun onTick(millisUntilFinished: Long) {
                    TODO("Not yet implemented")
                }

                override fun onFinish() {
                    TODO("Not yet implemented")
                }
            }
            timer
        }

        fun pause() {

        }

        fun resume() {

        }

        fun stop() {

        }

        companion object {
            const val COUNT_DOWN_INTERVAL = 1000L

            fun newInstance(duration: Long): Timer {
                return Timer(duration)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newIntent(ctx: Context): Intent =
            Intent(ctx, PomodoroActivity::class.java)
    }
}
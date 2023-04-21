package com.rencaihu.timer.ui.ongoingtimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.rencaihu.common.BaseFragment
import com.rencaihu.common.ext.parcelable
import com.rencaihu.design.IClock
import com.rencaihu.timer.R
import com.rencaihu.timer.databinding.ActivityBaseFocusBinding
import com.rencaihu.timer.ui.ongoingtimer.BaseFocusActivity.Companion.EXTRA_FOCUS
import timber.log.Timber

abstract class BaseFocusFragment: BaseFragment<ActivityBaseFocusBinding>() {

    private lateinit var focus: BaseFocus
    private lateinit var clock: IClock

    abstract fun getClockLayoutResource(): Int

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityBaseFocusBinding =
        ActivityBaseFocusBinding.inflate(inflater)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewStub.inflatedId = R.id.clock
        binding.viewStub.layoutResource = getClockLayoutResource()
        clock =
            (binding.viewStub.inflate().apply {
                // constraint controls to newly inflated clock
                binding.controls.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    topToBottom = R.id.clock
                    bottomToTop = binding.settings.id
                }
            }) as IClock
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        focus = getFocus(savedInstanceState)
        initFocus()
        setListeners()
    }

    open fun getFocus(savedInstanceState: Bundle?): BaseFocus {
        return savedInstanceState?.parcelable(EXTRA_FOCUS) ?: arguments?.parcelable(
            EXTRA_FOCUS
        ) ?: throw Error("$arguments is null")
    }

    private fun initFocus() {
        Timber.d("${focus.status}")
        clock.setProgress(focus.progress)
        clock.setLapDuration(focus.lapDuration)
        clock.setLaps(focus.laps)
        focus.setOnTickListener {
            Timber.d("progress: ${focus.progress}")
            clock.setProgress(++focus.progress)
        }
        focus.setOnFinishListener {

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(BaseFocusActivity.EXTRA_FOCUS, focus)
    }

    override fun onStart() {
        super.onStart()
        focus.start()
    }

    override fun onResume() {
        super.onResume()
        focus.recalibrateProgressAndResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        focus.recordTimeUponBackground()
    }

    private fun setListeners() {
        binding.btnPause.setOnClickListener {
            binding.switcher.showNext()
            focus.pause()
        }

        binding.btnResume.setOnClickListener {
            binding.switcher.showNext()
            focus.resume()
        }

        binding.btnStop.setOnClickListener {
            focus.stop()
        }
    }


}
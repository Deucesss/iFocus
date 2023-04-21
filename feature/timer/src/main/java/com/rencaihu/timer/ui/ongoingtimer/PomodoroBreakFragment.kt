package com.rencaihu.timer.ui.ongoingtimer

import android.view.LayoutInflater
import android.view.ViewGroup
import com.rencaihu.common.BaseFragment
import com.rencaihu.timer.databinding.FragmentPomodoroBreakBinding

class PomodoroBreakFragment: BaseFragment<FragmentPomodoroBreakBinding>() {

        override fun getViewBinding(
            inflater: LayoutInflater,
            container: ViewGroup?
        ): FragmentPomodoroBreakBinding =
            FragmentPomodoroBreakBinding.inflate(inflater, container, false)

}
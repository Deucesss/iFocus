package com.rencaihu.timer.ui.timersetup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rencaihu.common.BaseFragment
import com.rencaihu.timer.databinding.FragmentCountUpSetupBinding

class CountUpSetupFragment: BaseFragment<FragmentCountUpSetupBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCountUpSetupBinding =
        FragmentCountUpSetupBinding.inflate(layoutInflater)
}
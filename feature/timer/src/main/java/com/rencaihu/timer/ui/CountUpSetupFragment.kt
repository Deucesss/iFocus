package com.rencaihu.timer.ui

import android.os.Bundle
import android.view.View
import com.rencaihu.common.BaseFragment
import com.rencaihu.timer.databinding.FragmentCountUpSetupBinding

class CountUpSetupFragment: BaseFragment<FragmentCountUpSetupBinding>() {

    override fun getViewBinding(): FragmentCountUpSetupBinding =
        FragmentCountUpSetupBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
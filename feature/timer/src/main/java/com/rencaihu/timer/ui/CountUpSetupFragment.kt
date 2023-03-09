package com.rencaihu.timer.ui

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

class CountUpSetupFragment: BaseFragment<LayoutTimerSetupBinding>() {

    override fun getViewBinding(): LayoutTimerSetupBinding =
        LayoutTimerSetupBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
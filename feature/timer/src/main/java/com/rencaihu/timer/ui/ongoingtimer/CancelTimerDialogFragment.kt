package com.rencaihu.timer.ui.ongoingtimer

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.rencaihu.common.BaseDialogFragment
import com.rencaihu.timer.R

class CancelTimerDialogFragment: BaseDialogFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitle.text = "Stop Timer"

        binding.tvMessage.text = "Are you sure you want to stop this timer?"

        binding.btnLeft.setOnClickListener {
            dismiss()
        }

        binding.btnRight.setOnClickListener {
            // TODO: finish timer with TimerManager
            findNavController().navigate(R.id.action_cancelTimerDialogFragment_to_focusCompleteFragment)
        }
    }

}
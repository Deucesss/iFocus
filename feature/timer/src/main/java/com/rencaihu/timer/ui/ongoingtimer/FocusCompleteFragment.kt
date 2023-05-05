package com.rencaihu.timer.ui.ongoingtimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.rencaihu.common.BaseFragment
import com.rencaihu.timer.data.TimerManager
import com.rencaihu.timer.databinding.FragmentFocusCompleteBinding
import timber.log.Timber

class FocusCompleteFragment: BaseFragment<FragmentFocusCompleteBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFocusCompleteBinding =
        FragmentFocusCompleteBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding.btnDone.setOnClickListener {
            findNavController().backQueue.forEach {
                Timber.d("backQueue: ${it.destination.displayName}")
            }
            TimerManager.timerManager.removeTimerFromDatastore()
            requireActivity().finish()
        }
    }
}
package com.rencaihu.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rencaihu.common.BaseFragment
import com.rencaihu.timer.databinding.FragmentChooseTimerBinding

class ChooseTimerFragment: BaseFragment<FragmentChooseTimerBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseTimerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}
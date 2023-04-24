package com.rencaihu.timer.ui.ongoingtimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rencaihu.common.BaseFragment
import com.rencaihu.timer.databinding.FragmentFocusCompleteBinding

class FocusCompleteFragment: BaseFragment<FragmentFocusCompleteBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFocusCompleteBinding =
        FragmentFocusCompleteBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
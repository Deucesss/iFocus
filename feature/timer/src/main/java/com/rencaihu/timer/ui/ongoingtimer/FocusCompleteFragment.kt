package com.rencaihu.timer.ui.ongoingtimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rencaihu.common.BaseFragment
import com.rencaihu.common.ext.parcelable
import com.rencaihu.timer.EXTRA_FOCUS
import com.rencaihu.timer.databinding.FragmentFocusCompleteBinding
import timber.log.Timber

class FocusCompleteFragment: BaseFragment<FragmentFocusCompleteBinding>() {

    private lateinit var focus: BaseFocus

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFocusCompleteBinding =
        FragmentFocusCompleteBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        focus =
            savedInstanceState?.parcelable(EXTRA_FOCUS) ?: arguments?.parcelable(EXTRA_FOCUS) ?: throw Error("")

        Timber.d("$focus")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
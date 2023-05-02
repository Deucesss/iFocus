package com.rencaihu.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.rencaihu.common.databinding.FragmentBaseDialogBinding
import com.rencaihu.common.ext.screenWidth

abstract class BaseDialogFragment: DialogFragment() {

    protected lateinit var binding: FragmentBaseDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        }

        binding = FragmentBaseDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(requireContext().screenWidth(.8f), ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}


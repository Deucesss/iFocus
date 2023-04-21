package com.rencaihu.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import com.rencaihu.common.BaseFragment
import com.rencaihu.profile.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater)
}
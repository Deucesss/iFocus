package com.rencaihu.profile

import androidx.fragment.app.Fragment
import com.rencaihu.common.BaseFragment
import com.rencaihu.profile.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override fun getViewBinding(): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater)
}
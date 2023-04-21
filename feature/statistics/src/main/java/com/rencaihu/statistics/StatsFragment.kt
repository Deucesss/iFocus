package com.rencaihu.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.rencaihu.common.BaseFragment
import com.rencaihu.statistics.databinding.FragmentStatsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatsFragment: BaseFragment<FragmentStatsBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStatsBinding =
        FragmentStatsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnClickListener {
            val req =
                NavDeepLinkRequest.Builder
                    .fromUri("android-app://example.google.app/home_fragment".toUri())
                    .build()
            findNavController().navigate(req)
        }
    }
}
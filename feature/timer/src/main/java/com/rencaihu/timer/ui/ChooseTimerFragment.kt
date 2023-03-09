package com.rencaihu.timer.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.rencaihu.common.BaseFragment
import com.rencaihu.timer.databinding.FragmentChooseTimerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseTimerFragment: BaseFragment<FragmentChooseTimerBinding>() {
    override fun getViewBinding(): FragmentChooseTimerBinding =
        FragmentChooseTimerBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabLayoutWithViewPager()
    }

    private fun setupTabLayout() {
    }

    private fun setupTabLayoutWithViewPager() {
        binding.viewpager.adapter = ChooseTimerStateAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, pos ->
            tab.text =
                when (pos) {
                    0 -> "Up"
                    1 -> "Down"
                    2 -> "Tomato"
                    else -> throw Exception("")
                }
        }.attach()
    }

    private class ChooseTimerStateAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment =
            when (position) {
                0 -> CountUpSetupFragment()
                1 -> CountDownSetupFragment()
                2 -> TomatoSetupFragment()
                else -> throw Exception("")
            }
    }
}
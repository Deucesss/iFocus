package com.rencaihu.timer.ui.timersetup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.rencaihu.common.BaseFragment
import com.rencaihu.design.R
import com.rencaihu.timer.databinding.FragmentChooseTimerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseTimerFragment: BaseFragment<FragmentChooseTimerBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChooseTimerBinding =
        FragmentChooseTimerBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabLayoutWithViewPager()
        binding.navDrawer.setupWithNavController(findNavController())
        binding.toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_menu)!!.mutate().apply {
            setTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        }
        binding.toolbar.setNavigationOnClickListener {
            binding.root.openDrawer(binding.navDrawer)
        }
    }

    private fun setupTabLayoutWithViewPager() {
        // remove overscroll effect
        (binding.viewpager.getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
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
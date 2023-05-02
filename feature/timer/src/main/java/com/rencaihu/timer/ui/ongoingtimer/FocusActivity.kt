package com.rencaihu.timer.ui.ongoingtimer

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.rencaihu.common.BaseActivity
import com.rencaihu.timer.R
import com.rencaihu.timer.databinding.ActivityFocusBinding
import com.rencaihu.timer.ui.ongoingtimer.TimerFragment.Companion.EXTRA_TIMER

class FocusActivity: BaseActivity<ActivityFocusBinding>() {

    override fun getViewBinding(): ActivityFocusBinding =
        ActivityFocusBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navController = (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
        val navGraph = navController.navInflater.inflate(R.navigation.focus_nav_graph)
        val timerId = intent.getIntExtra(EXTRA_TIMER, 0)
        if (timerId != 0) {
            navGraph.setStartDestination(R.id.timerFragment)
            navController.setGraph(navGraph, intent.extras)
            return
        }
    }
}
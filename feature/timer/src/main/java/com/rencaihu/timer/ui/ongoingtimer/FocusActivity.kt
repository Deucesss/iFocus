package com.rencaihu.timer.ui.ongoingtimer

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.rencaihu.common.BaseActivity
import com.rencaihu.common.ext.parcelable
import com.rencaihu.timer.EXTRA_FOCUS
import com.rencaihu.timer.R
import com.rencaihu.timer.databinding.ActivityFocusBinding

class FocusActivity: BaseActivity<ActivityFocusBinding>() {

    override fun getViewBinding(): ActivityFocusBinding =
        ActivityFocusBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navController = (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
        val navGraph = navController.navInflater.inflate(R.navigation.focus_nav_graph)
        navGraph.setStartDestination(
            when (intent.parcelable<BaseFocus>(EXTRA_FOCUS) ?: return) {
                is UpFocus -> 0
                is DownFocus -> R.id.countDownFragment
            }
        )
        navController.setGraph(navGraph, intent.extras)
    }

}
package com.rencaihu.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T: ViewBinding>: AppCompatActivity() {

    protected lateinit var binding: T

    abstract fun getViewBinding(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = getViewBinding()
        setContentView(binding.root)
    }

}
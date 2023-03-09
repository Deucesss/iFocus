package com.rencaihu.ifocus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.rencaihu.ifocus.databinding.ActivityMainBinding
import com.rencaihu.lib_common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}
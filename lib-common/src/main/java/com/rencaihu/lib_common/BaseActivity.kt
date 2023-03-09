package com.rencaihu.lib_common

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T: ViewBinding>: AppCompatActivity() {

    protected lateinit var binding: T



}
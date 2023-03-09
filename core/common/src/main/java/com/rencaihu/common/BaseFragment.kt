package com.rencaihu.common

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T: ViewBinding>: Fragment() {
    protected var _binding: T? = null
    val binding: T get() = _binding!!
}
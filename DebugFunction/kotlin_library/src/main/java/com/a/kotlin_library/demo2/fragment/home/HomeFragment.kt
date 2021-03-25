package com.a.kotlin_library.demo2.fragment.home

import com.a.kotlin_library.R
import com.a.kotlin_library.databinding.FragmentHomeBinding
import com.a.kotlin_library.demo2.fragment.base.BaseFragment


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    private val TAG = "HomeFragment"
    override fun layoutId(): Int = R.layout.fragment_home
    override fun initView() {
    }
}

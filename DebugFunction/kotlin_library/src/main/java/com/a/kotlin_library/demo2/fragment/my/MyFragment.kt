package com.a.kotlin_library.demo2.fragment.my

import com.a.kotlin_library.R
import com.a.kotlin_library.databinding.FragmentMyBinding
import com.a.kotlin_library.demo2.fragment.base.BaseFragment

class MyFragment : BaseFragment<FragmentMyBinding, MyViewModel>() {
    override fun layoutId(): Int = R.layout.fragment_my

    override fun initView() {
    }
}

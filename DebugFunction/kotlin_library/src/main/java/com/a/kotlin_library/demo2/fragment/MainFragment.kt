package com.a.kotlin_library.demo2.fragment

import com.a.kotlin_library.R
import com.a.kotlin_library.databinding.FragmentMainBinding
import com.a.kotlin_library.demo2.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment<FragmentMainBinding, FragmentMainViewModel>() {

    override fun layoutId() = R.layout.fragment_main

    override fun initView() {
        mainViewpager.initMain(this)
        mainBottom.init { it ->
            when (it) {
                R.id.menu_main -> mainViewpager.setCurrentItem(0, false)
                R.id.menu_project -> mainViewpager.setCurrentItem(1, false)
                R.id.menu_system -> mainViewpager.setCurrentItem(2, false)
                R.id.menu_public -> mainViewpager.setCurrentItem(3, false)
                R.id.menu_me -> mainViewpager.setCurrentItem(4, false)
            }
        }
        mainBottom.interceptLongClick(R.id.menu_main, R.id.menu_project, R.id.menu_system, R.id.menu_public, R.id.menu_me)

    }
}

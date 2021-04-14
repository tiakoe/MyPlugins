package com.a.kotlin_library.demo2.fragment.project

import android.os.Bundle
import com.a.kotlin_library.R
import com.a.kotlin_library.databinding.FragmentProjectBinding
import com.a.kotlin_library.demo2.fragment.base.BaseFragment

class ProjectFragment : BaseFragment<FragmentProjectBinding, ProjectViewModel>() {
    override fun layoutId(): Int = R.layout.fragment_project

    override fun initView(savedInstanceState: Bundle?) {

    }
}

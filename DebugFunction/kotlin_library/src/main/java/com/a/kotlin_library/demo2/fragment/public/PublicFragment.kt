package com.a.kotlin_library.demo2.fragment.public

import android.os.Bundle
import com.a.kotlin_library.R
import com.a.kotlin_library.databinding.FragmentPublicBinding
import com.a.kotlin_library.demo2.fragment.base.BaseFragment


class PublicFragment : BaseFragment<FragmentPublicBinding, PublicViewModel>() {
    override fun layoutId(): Int = R.layout.fragment_public

    override fun initView(savedInstanceState: Bundle?) {

    }
}

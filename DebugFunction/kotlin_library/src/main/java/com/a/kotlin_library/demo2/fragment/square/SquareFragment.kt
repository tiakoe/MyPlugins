package com.a.kotlin_library.demo2.fragment.square

import android.widget.CompoundButton
import androidx.databinding.Bindable
import com.a.kotlin_library.R
import com.a.kotlin_library.databinding.FragmentSquareBinding
import com.a.kotlin_library.demo2.fragment.base.BaseFragment
import com.a.kotlin_library.demo2.fragment.register.RegisterViewModel


class SquareFragment : BaseFragment<FragmentSquareBinding, RegisterViewModel>() {
    override fun layoutId(): Int = R.layout.fragment_square

    override fun initView() {

    }


}

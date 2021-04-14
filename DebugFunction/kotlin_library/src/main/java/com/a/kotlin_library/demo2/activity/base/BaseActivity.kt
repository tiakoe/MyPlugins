package com.a.kotlin_library.demo2.activity.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.a.kotlin_library.demo2.utils.getVmClazz

abstract class BaseActivity<DB : ViewDataBinding, VM : ViewModel> : AppCompatActivity() {

    lateinit var binding: DB
    lateinit var viewModel: VM

    abstract fun layoutId(): Int
    abstract fun beforeInit(savedInstanceState: Bundle?)
    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun bindViewModel()
    abstract fun observe()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeInit(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId())
        viewModel = ViewModelProvider(this).get(getVmClazz(this))
        bindViewModel()
        initView(savedInstanceState)
        observe()
    }

}

package com.a.kotlin_library.demo2.activity.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseActivity<DB : ViewDataBinding, VM : ViewModel> : AppCompatActivity() {

    lateinit var binding: DB
    lateinit var viewModel: VM

    abstract fun layoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId())
    }

//    @Suppress("UNCHECKED_CAST")
//    fun <VM> getVmClazz(obj: Any): VM {
//        return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
//    }
}

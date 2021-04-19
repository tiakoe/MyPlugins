package com.a.kotlin_library.demo2.fragment.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<DB : ViewDataBinding, VM : ViewModel> : Fragment() {

    lateinit var mContext: AppCompatActivity
    lateinit var viewModel: VM
    lateinit var binding: DB

    abstract fun layoutId(): Int
    abstract fun initView(savedInstanceState: Bundle?)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context as AppCompatActivity
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(getVmClazz(this))
        initView(savedInstanceState)
    }

    @Suppress("UNCHECKED_CAST")
    fun <VM> getVmClazz(obj: Any): VM {
        return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as VM
    }

}

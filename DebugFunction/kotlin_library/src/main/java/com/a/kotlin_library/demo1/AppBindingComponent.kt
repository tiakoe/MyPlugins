package com.a.kotlin_library.demo1

import androidx.databinding.DataBindingComponent
import androidx.lifecycle.LifecycleCoroutineScope


class AppBindingComponent(private val scope: LifecycleCoroutineScope) : DataBindingComponent {
    override fun getAppViewBinding(): AppViewBinding {
        return AppViewBindingImpl(scope)
    }
}

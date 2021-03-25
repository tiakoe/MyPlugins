package com.a.kotlin_library.demo1

import androidx.databinding.DataBindingComponent
import androidx.lifecycle.LifecycleCoroutineScope


class AppBindingComponent(val scope: LifecycleCoroutineScope) : DataBindingComponent {
    override fun getAppViewBinding(): AppViewBinding {
        return AppViewBindingImpl(scope)
    }
}

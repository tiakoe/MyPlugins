package com.a.kotlin_library.demo2.base

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

open class BaseApp : Application(), ViewModelStoreOwner {

    lateinit var mAppViewModelStore: ViewModelStore

    var mFactory: ViewModelProvider.Factory? = null

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }

    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()
    }

    /**
     * 获取一个全局的ViewModel
     */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }
}

package com.a.kotlin_library.demo2.application

import android.app.Application
import com.a.kotlin_library.demo2.retrofit.ApiService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApplication : Application() {

    private val listOfModules = module {
        single { ApiService }
    }

    override fun onCreate() {
        super.onCreate()

        mApplication = this

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(listOfModules)
        }
    }

    companion object {
        private var mApplication: Application? = null

        fun getApplication(): Application? {
            return mApplication
        }
    }

}

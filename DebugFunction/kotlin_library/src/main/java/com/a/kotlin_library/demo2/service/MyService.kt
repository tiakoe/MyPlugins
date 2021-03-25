package com.a.kotlin_library.demo2.service

import android.content.Intent
import android.view.View
import androidx.lifecycle.LifecycleService

class MyService : LifecycleService() {
    private var myServiceObserver: MyServiceObserver = MyServiceObserver()

    init {
        lifecycle.addObserver(myServiceObserver)
    }

    fun launchService(view: View) {
        startService(Intent(this, MyService::class.java))
    }

    fun closeService(view: View) {
        stopService(Intent(this, MyService::class.java))
    }
}



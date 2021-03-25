package com.a.kotlin_library.demo2.service

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MyServiceObserver : LifecycleObserver {
    private val TAG = "MyServiceObserver"

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Log.e(TAG, "start: 执行")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Log.e(TAG, "onDestroy: 执行")
    }

}

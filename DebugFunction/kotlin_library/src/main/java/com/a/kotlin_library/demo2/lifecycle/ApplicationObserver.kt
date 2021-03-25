package com.a.kotlin_library.demo2.lifecycle

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class ApplicationObserver : LifecycleObserver {
    private val TAG = "ApplicationObserver"

    //  整个生命周期只会调用一次
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreated() {
        Log.d(TAG, "onCreated: 执行")
    }

    //    监听应用程序的处于前台时调用
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Log.d(TAG, "onStart: 前台执行")
    }

    //    监听应用程序的处于前台时调用
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Log.d(TAG, "onResume: 前台执行")
    }

    //    监听应用程序的处于后台时调用
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Log.d(TAG, "onPause: 后台执行")
    }

    //    监听应用程序的处于后台时调用
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Log.d(TAG, "onStop: 后台执行")
    }

    //   永远不会调用，系统不会分发调用on_destroy事件
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Log.d(TAG, "onDestroy: 不会执行")
    }
}


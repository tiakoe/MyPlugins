package com.a.kotlin_library.demo2.base.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.*


class MyActivityManager : Application.ActivityLifecycleCallbacks {

    var activities: Stack<Activity> = Stack()

    companion object {
        val instance: MyActivityManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { MyActivityManager() }
    }

    fun getActivityStack(): Stack<Activity> {
        return activities
    }

    fun addActivity(activity: Activity?) {
        activities.add(activity)
    }

    fun removeActivity(activity: Activity?) {
        if (activity != null) {
            activities.remove(activity)
        }
    }

    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            removeActivity(activity)
            activity.finish()
        }
    }

    //    获取当前Activity
    fun getCurrentActivity(): Activity? {
        return activities.lastElement()
    }

    //    结束当前Activity
    fun finishActivity() {
        finishActivity(activities.lastElement())
    }


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}

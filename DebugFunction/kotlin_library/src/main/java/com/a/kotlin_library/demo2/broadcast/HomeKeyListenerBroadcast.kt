package com.a.kotlin_library.demo2.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter


class HomeKeyListenerBroadcast : BroadcastReceiver {
    private var context: Context?
    private var onHomeKeyPressListener: OnHomeKeyPressListener? = null
    private var isRegiested = false

    constructor(context: Context?) {
        this.context = context
    }

    constructor(context: Context?, onHomeKeyPressListener: OnHomeKeyPressListener?) {
        this.context = context
        this.onHomeKeyPressListener = onHomeKeyPressListener
    }

    fun setOnHomeKeyPressListener(onHomeKeyPressListener: OnHomeKeyPressListener?) {
        this.onHomeKeyPressListener = onHomeKeyPressListener
    }

    fun registerBroadcast() {
        if (context != null) {
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
            context!!.registerReceiver(this, filter)
            isRegiested = true
        }
    }

    fun unRegisterBroadcast() {
        if (isRegiested) {
            context?.unregisterReceiver(this)
            isRegiested = false
        }
    }

    override fun onReceive(context: Context?, intent: Intent) {
        val action = intent.action
        if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS == action) {
            val reason = intent.getStringExtra("reason")
            if ("homekey" == reason) {
                // 按下HOME健
                if (onHomeKeyPressListener != null) {
                    onHomeKeyPressListener!!.onHomeKeyPress()
                }
            } else if ("recentapps" == reason) {
                // 长按HOME键
                if (onHomeKeyPressListener != null) {
                    onHomeKeyPressListener!!.onRecentAppsKeyLongPress()
                }
            }
        }
    }

    //    事件回调接口
    interface OnHomeKeyPressListener {
        fun onHomeKeyPress()
        fun onRecentAppsKeyLongPress()
    }
}

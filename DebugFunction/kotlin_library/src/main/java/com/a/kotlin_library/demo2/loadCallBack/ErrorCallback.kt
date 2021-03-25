package com.a.kotlin_library.demo2.loadCallBack

import com.a.kotlin_library.R
import com.kingja.loadsir.callback.Callback


class ErrorCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_error
    }

}

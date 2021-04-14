package com.a.kotlin_library.demo2.activity.other2.test4

import android.os.Bundle
import com.a.kotlin_library.R
import com.a.kotlin_library.databinding.Test4ActivityBinding
import com.a.kotlin_library.demo2.activity.base.BaseActivity

class Test4MainActivity : BaseActivity<Test4ActivityBinding, Test4ViewModel>() {
    override fun layoutId(): Int {
        return R.layout.test4_activity
    }

    override fun beforeInit(savedInstanceState: Bundle?) {
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun bindViewModel() {
    }

    override fun observe() {
    }
}

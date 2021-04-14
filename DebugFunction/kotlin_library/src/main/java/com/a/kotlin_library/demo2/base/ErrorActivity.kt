package com.a.kotlin_library.demo2.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.a.kotlin_library.R

class ErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
    }
}

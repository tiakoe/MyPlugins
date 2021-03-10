package com.a.kotlin_library.test2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.a.kotlin_library.R
import com.a.kotlin_library.databinding.ActivityFirstBinding

class MyFirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstBinding
    private lateinit var viewModel: ActivityFirstViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_first)
        viewModel = ViewModelProvider(this).get(ActivityFirstViewModel::class.java)
//        setContentView(R.layout.activity_first)
        binding.data1 = viewModel
    }
}


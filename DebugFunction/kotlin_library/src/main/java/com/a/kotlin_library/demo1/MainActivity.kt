package com.a.kotlin_library.demo1

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.a.kotlin_library.R
import com.a.kotlin_library.databinding.ActivityMainDemo1Binding

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var binding: ActivityMainDemo1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_main_demo1,
                AppBindingComponent(lifecycleScope)
        )

        binding.viewModel = viewModel
    }
}

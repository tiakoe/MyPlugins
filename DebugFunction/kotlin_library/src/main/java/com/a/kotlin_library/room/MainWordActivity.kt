//package com.a.kotlin_library.room
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//
//class MainWordActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        wordViewModel.allWords.observe(this) { words ->
//            words.let {
//                adapter.submitList(it)
//            }
//        }
//    }
//}

package com.a.kotlin_library.test2

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun log(msg: String) {
    println("[${Thread.currentThread().name}] $msg")
}

//运行在线程池中
fun main() = runBlocking {
    val job1 = GlobalScope.launch {
        log("launch before delay")
        delay(100)
        log("launch after delay")
    }
    val job2 = GlobalScope.launch {
        log("launch2 before delay")
        delay(200)
        log("launch2 after delay")
    }

    job1.join()
    job2.join()
}

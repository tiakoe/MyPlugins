package com.a.kotlin_library.语法

import android.widget.Button
import android.widget.TextView

class Producer<out T> {
    fun produce() {
    }
}

val producer: Producer<TextView> = Producer<Button>() // 👈 这里不写 out 也不会报错

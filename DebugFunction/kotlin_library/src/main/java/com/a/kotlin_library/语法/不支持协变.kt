package com.a.kotlin_library.语法

val fd = sequenceOf("a", "b", "c")
val list = listOf("a", "b", "c")
var fdfd = list.asSequence()

val sequence = generateSequence(0) { it + 1 }
// 👆 lambda 表达式，负责生成第二个及以后的元素，it 表示前一个元素

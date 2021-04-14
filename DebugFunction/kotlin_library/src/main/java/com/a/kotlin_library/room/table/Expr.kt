package com.a.kotlin_library.room.table

open class Expr {
    fun getId(expr: Expr): Int = when (expr) {
        is HomeTable -> expr.id
        is BannerTable -> expr.id
        else -> -1
    }

    fun getDesc(expr: Expr): String = when (expr) {
        is HomeTable -> expr.desc
        is BannerTable -> expr.desc
        else -> ""
    }
}





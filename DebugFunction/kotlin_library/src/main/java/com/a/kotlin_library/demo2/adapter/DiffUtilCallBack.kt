package com.a.kotlin_library.demo2.adapter

import androidx.recyclerview.widget.DiffUtil
import com.a.kotlin_library.room.table.Expr

class DiffUtilCallBack<T : Expr>(
        private val oldList: List<T>,
        private val newList: List<T>,
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].getId(oldList[oldItemPosition]) == newList[newItemPosition].getId(newList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].getDesc(oldList[oldItemPosition]) == newList[newItemPosition].getDesc(newList[newItemPosition])
    }
}

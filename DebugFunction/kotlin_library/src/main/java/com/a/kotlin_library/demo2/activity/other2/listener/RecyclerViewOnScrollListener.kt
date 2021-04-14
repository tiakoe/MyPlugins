package com.a.kotlin_library.demo2.activity.other2.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewOnScrollListener internal constructor(private var listener: OptionOnScrollListener) : RecyclerView.OnScrollListener() {
    var isUpScroll = false

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val manager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        if (newState == RecyclerView.SCROLL_STATE_IDLE) { //当前未滑动
            val lastItemPosition = manager.findLastCompletelyVisibleItemPosition()
            if (lastItemPosition == (manager.itemCount - 1) && isUpScroll) {
                listener.onLoadMore()
            }
            val firstItemPosition = manager.findFirstCompletelyVisibleItemPosition()
            if (firstItemPosition == 0 && !isUpScroll) {
                listener.onRefresh()
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        isUpScroll = dy > 0;
    }

}

package com.a.kotlin_library.demo2.activity.other2

import android.content.Context
import android.graphics.Rect
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a.kotlin_library.demo2.activity.other2.listener.OptionOnScrollListener
import java.lang.Thread.sleep


class CustomLinerLayout : LinearLayout {
    private var childView: RecyclerView? = null
    private val original = Rect()
    private var isMoved = false
    private var startYpos = 0f
    private var isSuccess = false
    private var mScrollListener: OptionOnScrollListener? = null
    private lateinit var linearLayout: LinearLayoutManager

    private var pullDown = false
    private var pullUp = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        private const val ANIM_TIME = 500
        private const val DAMPING_COEFFICIENT = 0.3f //阻尼系数

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        childView = getChildAt(0) as RecyclerView
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        original[childView!!.left, childView!!.top, childView!!.right] = childView!!.bottom
    }

    fun setScrollListener(listener: OptionOnScrollListener?) {
        mScrollListener = listener
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val touchYpos = ev.y
        if (touchYpos >= original.bottom || touchYpos <= original.top) {
            if (isMoved) {
                recoverLayout()
            }
            return true
        }
        return when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startYpos = ev.y
                isMoved = false
                isSuccess = true
                super.dispatchTouchEvent(ev)
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = (ev.y - startYpos).toInt()
                pullDown = dy > 0 && canPullDown()
                pullUp = dy < 0 && canPullUp()
                if (pullDown || pullUp) {
                    cancelChild(ev)
                    val offset = (dy * DAMPING_COEFFICIENT).toInt()
                    childView!!.layout(original.left, original.top + offset, original.right,
                            original.bottom + offset)
                    isMoved = true
                    isSuccess = false
                    true
                } else {
                    startYpos = ev.y
                    isMoved = false
                    isSuccess = true
                    super.dispatchTouchEvent(ev)
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isMoved) {
                    recoverLayout()
                }
                !isSuccess || super.dispatchTouchEvent(ev)
            }
            else -> true
        }
    }

    /**
     * 取消子view已经处理的事件
     *
     * @param ev event
     */
    private fun cancelChild(ev: MotionEvent) {
        ev.action = MotionEvent.ACTION_CANCEL
        super.dispatchTouchEvent(ev)
    }

    /**
     * 位置还原
     */
    var t: Long = 2 * 60 * 60 * 1000 //定义总时长 2小时
    var countDownTimer = object : CountDownTimer(t, 300) {
        override fun onFinish() {
        }

        override fun onTick(millisUntilFinished: Long) {
            var hour = millisUntilFinished / 1000 / 60 / 60
            var minute = millisUntilFinished / 1000 / 60 % 60
            var second = millisUntilFinished / 1000 % 60
        }
    }

    private fun recoverLayout() {
        val anim = TranslateAnimation(0F, 0F, (childView!!.top - original.top).toFloat(),
                0F)
        anim.duration = ANIM_TIME.toLong()
        childView!!.startAnimation(anim)
        sleep(4000)

        childView!!.layout(original.left, original.top, original.right, original.bottom)
        isMoved = false
    }

    private fun canPullDown(): Boolean {
        val firstVisiblePosition = (childView!!.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
        if (firstVisiblePosition != 0 && childView!!.adapter!!.itemCount != 0) {
            return false
        }
        val mostTop = if (childView!!.childCount > 0) childView!!.getChildAt(0).top else 0
        return mostTop >= 0
    }

    private fun canPullUp(): Boolean {
        linearLayout = childView!!.layoutManager as LinearLayoutManager
        val firstVisiblePosition = linearLayout.findFirstVisibleItemPosition()
        val lastVisiblePosition = linearLayout.findLastVisibleItemPosition()
        val lastItemPosition = childView!!.adapter!!.itemCount - 1
        if (lastVisiblePosition >= lastItemPosition) {
            val childIndex = lastVisiblePosition - firstVisiblePosition
            val childCount = childView!!.childCount
            val index = Math.min(childIndex, childCount - 1)
            val lastVisibleChild = childView!!.getChildAt(index)
            if (lastVisibleChild != null) {
                return lastVisibleChild.bottom <= childView!!.bottom - childView!!.top
            }
        }
        return false
    }


}

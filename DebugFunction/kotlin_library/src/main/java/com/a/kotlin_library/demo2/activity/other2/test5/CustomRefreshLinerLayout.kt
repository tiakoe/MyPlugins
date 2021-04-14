package com.a.kotlin_library.demo2.activity.other2.test5

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import androidx.annotation.IntDef
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a.kotlin_library.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.math.min
import kotlin.properties.Delegates

const val TimeOut = 1
const val RequestTimeOut = 2
const val OtherError = 3


@IntDef(value = [TimeOut, RequestTimeOut, OtherError])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class RequestStatus

class CustomRefreshLinerLayout : LinearLayout {
    private lateinit var contentView: RecyclerView
    private lateinit var mRefreshView: View
    private lateinit var mLoadView: View
    private lateinit var curItemView: View

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor {
        Thread(it, "SingleThread")
    }

    private var timer: Timer? = null
    private lateinit var timerTask: TimerTask


    private var submit: Future<*>? = null

    private var mCurState = 0
    private var NONE_STATE = 0

    var REFRESH_DISPLAY_ALL = 1 shl 0
    var REFRESH_DISPLAY_HALF = 1 shl 1
    var REFRESH_SUCCESS = 1 shl 2
    var REFRESH_FAILURE = 1 shl 3

    var LOAD_DISPLAY_ALL = 1 shl 4
    var LOAD_DISPLAY_HALF = 1 shl 5
    var LOAD_SUCCESS = 1 shl 6
    var LOAD_FAILURE = 1 shl 7

    var mCurAnimState = 2
    var ANIM_STATUS_NONE = 2 shl 0
    var ANIM_STATUS_ERROR = 2 shl 1
    var ANIM_STATUS_SUCCESS = 2 shl 2

    //    中断:取消刷新或加载动作
    var ANIM_STATUS_INTERRUPT = 2 shl 3

    //    展示刷新或者加载数据的动画
    var ANIM_STATUS_DISPLAY = 2 shl 4

    //    拖动释放-》动画停止
    var ANIM_STATUS_DRAG_ANIM = 2 shl 5

    //    拖动中
    var ANIM_STATUS_DRAG = 2 shl 8

    //    拖动释放时刻
    var ANIM_STATUS_DRAG_ANIM_START = 2 shl 7

    //    恢复item
    var ANIM_STATUS_RECOVER = 2 shl 6

    private var mRefreshTriggerHeight = 150
    private var mLoadTriggerHeight = 150
    private var mAnimTime = 500

    private lateinit var mRequestListener: OnRequestDataStateListener

    private lateinit var mRefreshListener: OnRefreshStateListener
    private lateinit var mLoadListener: OnLoadStateListener
    private lateinit var mRecycleListener: RecycleListener

    private val contentOri = Rect()
    private val refreshOri = Rect()
    private val loadOri = Rect()
    private var oldRect = Rect()


    private var curState by Delegates.notNull<Int>()
    private var offset by Delegates.notNull<Int>()

    private var oldy = 0f
    private lateinit var linearLayout: LinearLayoutManager

    private var pullDown = false
    private var pullUp = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val ta = context!!.obtainStyledAttributes(attrs, R.styleable.CustomRefreshLinerLayout)
        mRefreshTriggerHeight = ta.getDimension(R.styleable.CustomRefreshLinerLayout_refreshTriggerHeight, 150F).toInt()
        mLoadTriggerHeight = ta.getDimension(R.styleable.CustomRefreshLinerLayout_loadTriggerHeight, 150F).toInt()
        mAnimTime = ta.getDimension(R.styleable.CustomRefreshLinerLayout_animTime, 500F).toInt()
        ta.recycle()

    }

    companion object {
        private const val DAMPING_COEFFICIENT = 0.3f //阻尼系数

    }

    val valueAnimator = ValueAnimator()
    val valueAnimator2 = ValueAnimator()


    init {
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.duration = 500
        valueAnimator2.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator2.duration = 500
    }

    private val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
        }
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        mRefreshView = getChildAt(0)
        contentView = getChildAt(1) as RecyclerView
        mLoadView = getChildAt(2)
        curItemView = mRefreshView
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        refreshOri[paddingLeft, -mRefreshView.measuredHeight, paddingLeft + mRefreshView.measuredWidth] = 0

        contentOri[l, 0, r] = measuredHeight

        if (oldRect.top == 0) {
            oldRect[l, 0, r] = measuredHeight
        }

        loadOri[paddingLeft, measuredHeight, paddingLeft + mLoadView.measuredWidth] = measuredHeight + mLoadView.measuredHeight


        mRefreshView.layout(refreshOri.left, refreshOri.top, refreshOri.right, refreshOri.bottom)
        contentView.layout(contentOri.left, contentOri.top, contentOri.right, contentOri.bottom)
        mLoadView.layout(loadOri.left, loadOri.top, loadOri.right, loadOri.bottom)
    }

    fun setRefreshListener(refreshListener: OnRefreshStateListener) {
        this.mRefreshListener = refreshListener
    }

    fun setLoadListener(loadListener: OnLoadStateListener) {
        this.mLoadListener = loadListener
    }

    fun setRequestListener(requestListener: OnRequestDataStateListener) {
        this.mRequestListener = requestListener
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        // ev.y :触摸点到视图框顶部的距离
        return when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                oldy = ev.y
                curState = 0
                mCurState = 0
                cancelAnimation()
                mHandler.removeCallbacksAndMessages(null)
                super.dispatchTouchEvent(ev)
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = (ev.y - oldy).toInt()
                pullDown = dy > 0 && canPullDown()
                pullUp = dy < 0 && canPullUp()
                offset = (dy * DAMPING_COEFFICIENT).toInt()

                var cTop = oldRect.top + offset
                var cBot = oldRect.bottom + offset
                if (oldRect.top > mRefreshView.measuredHeight) {
                    cTop = (oldRect.top + offset * 0.8).toInt()
                } else if (oldRect.bottom < contentView.measuredHeight - mLoadView.measuredHeight) {
                    cBot = (oldRect.bottom + offset * 0.8).toInt()
                }


                contentView.layout(contentOri.left, cTop, contentOri.right, cBot)


                if (pullDown || pullUp) {
                    if (oldRect.top < 10) {
                        updateViewLayout(offset)
                    }
                    mCurAnimState = ANIM_STATUS_DRAG
                    mRecycleListener.onDragChange(curItemView, mCurAnimState)
                    true
                } else {
                    if (oldRect.top < 10) {
                        mRefreshView.layout(refreshOri.left, contentView.top - mRefreshView.measuredHeight, refreshOri.right, contentView.top)
                        mLoadView.layout(loadOri.left, contentView.bottom, loadOri.right, contentView.bottom + mLoadView.measuredHeight)
                    }

                    mCurAnimState = ANIM_STATUS_NONE
                    mHandler.removeCallbacksAndMessages(null)
                    // TODO: 2021/4/1 解除动画，取消刷新
                    oldy = ev.y
                    curState = 0
                    super.dispatchTouchEvent(ev)
                }
            }
            MotionEvent.ACTION_UP -> {
                cancelAnimation()
                mCurAnimState = ANIM_STATUS_NONE
                val state = mCurState
                if (curState > 0) {
                    var dy = offset
                    var leftover = 0
                    if (state == REFRESH_DISPLAY_ALL || state == LOAD_DISPLAY_ALL) {
                        dy = if (offset > 0) {
                            leftover = refreshOri.bottom - refreshOri.top
                            offset - leftover
                        } else {
                            leftover = -loadOri.bottom + loadOri.top
                            offset - leftover
                        }
                    }

                    showTipAnimation(dy, leftover)

                    valueAnimator2.doOnEnd {
                        oldRect[contentView.left, 0, contentView.right] = contentView.measuredHeight
                    }

                    valueAnimator2.setIntValues(leftover, 0)
                    valueAnimator2.addUpdateListener {
//                        开始收回item
                        val value: Int = it.animatedValue as Int
                        contentView.layout(contentOri.left, contentOri.top + value, contentOri.right, contentOri.bottom + value)
                        updateViewLayout(value)
                        oldRect[contentView.left, contentView.top, contentView.right] = contentView.bottom
                        mCurAnimState = ANIM_STATUS_RECOVER
                        mRecycleListener.onAnimComplete(curItemView, mCurAnimState)
                    }

                    if (mCurState == REFRESH_DISPLAY_ALL || mCurState == LOAD_DISPLAY_ALL) {

                        if (!executorService.isTerminated) {
                            if (submit == null || submit!!.isDone || submit!!.isCancelled) {
                                timer = Timer()
                                submit = executorService.submit<Boolean> {
                                    Thread.sleep((1000..5000).random().toLong())
                                    val isSuccess = mRequestListener.request()
                                    if (isSuccess) {
                                    } else {
                                        // TODO: 2021/4/1 刷新加载 失败的提示
                                    }
                                    mHandler.post(runnableStartAnim)
                                    timer?.cancel()
                                    timer = null
                                    true
                                }

                                timerTask = object : TimerTask() {
                                    override fun run() {
                                        if (!submit?.isDone!! || !submit?.isCancelled!!) {
                                            submit?.cancel(true)
                                            mHandler.post(runnableStartAnim)
                                        } else {
                                        }
                                    }
                                }
                                timer?.schedule(timerTask, 3000)
                            } else {
                            }
                        } else {
                        }

                    } else {
                        if (mCurState == REFRESH_DISPLAY_HALF || mCurState == LOAD_DISPLAY_HALF) {
                            valueAnimator2.start()
                        }
                    }

                    curState = 0
                    mCurState = 0
                } else {
                    fixContentLayout()
                    super.dispatchTouchEvent(ev)
                }
                true
            }
            else -> true
        }
    }

    val runnableStartAnim = Runnable {
        if (!valueAnimator2.isStarted && !valueAnimator2.isRunning) {
            valueAnimator2.start()
        }
    }


    private fun cancelAnimation() {
        if (valueAnimator.isStarted || valueAnimator.isRunning) {
            mCurAnimState = ANIM_STATUS_INTERRUPT
            valueAnimator.cancel()
            valueAnimator.removeAllUpdateListeners()
        }
        if (valueAnimator2.isStarted || valueAnimator2.isRunning) {
            mCurAnimState = ANIM_STATUS_INTERRUPT
            valueAnimator2.cancel()
            valueAnimator2.removeAllUpdateListeners()
        }
        contentView.layout(contentOri.left, oldRect.top, contentOri.right, oldRect.bottom)
    }

    private fun showTipAnimation(dy: Int, leftover: Int) {
        valueAnimator.setIntValues(dy, 0)
        valueAnimator.doOnEnd {
            mCurAnimState = ANIM_STATUS_DISPLAY
            mRecycleListener.onAnimDisplay(curItemView)
        }
        valueAnimator.doOnStart {
            mCurAnimState = ANIM_STATUS_DRAG_ANIM_START
            mRecycleListener.onDragAnimStart(curItemView, mCurAnimState)
        }
        valueAnimator.addUpdateListener {
            mCurAnimState = ANIM_STATUS_DRAG_ANIM
            mRecycleListener.onDragAnim(curItemView, mCurAnimState)
            // TODO: 2021/4/1 释放过程的动画
            var value: Int = it.animatedValue as Int
            value += leftover
            contentView.layout(contentOri.left, contentOri.top + value, contentOri.right, contentOri.bottom + value)
            updateViewLayout(value)
            if (value == 0) {
                //     头部显示出来，回到原来位置才会执行
            }
            oldRect[contentView.left, contentView.top, contentView.right] = contentView.bottom
        }
        if (!valueAnimator.isStarted && !valueAnimator.isRunning) {
            valueAnimator.start()
        }
    }

    private fun fixContentLayout() {
        if (mRefreshView.top != refreshOri.top) {
            mRefreshView.layout(refreshOri.left, refreshOri.top, refreshOri.right, refreshOri.bottom)
        }
        if (contentView.top != contentOri.top) {
            contentView.layout(contentOri.left, contentOri.top, contentOri.right, contentOri.bottom)
        }
        if (mLoadView.top != loadOri.top) {
            mLoadView.layout(loadOri.left, loadOri.top, loadOri.right, loadOri.bottom)
        }
    }

    private suspend fun requestData(): Boolean {
        val request = GlobalScope.launch {
            delay(50000)
        }.start()
        return request
    }

    private fun updateViewLayout(offset: Int) {
        curState = when {
            pullDown -> {
                val top: Int
                val bottom: Int
                when {
                    refreshOri.top + offset > 0 -> {
                        mCurState = REFRESH_DISPLAY_ALL
                        top = 0
                        bottom = mRefreshView.measuredHeight
                    }
                    else -> {
                        mCurState = REFRESH_DISPLAY_HALF
                        top = refreshOri.top + offset
                        bottom = mRefreshView.measuredHeight + offset
                    }
                }
                curItemView = mRefreshView
                mRecycleListener = mRefreshListener
                mRefreshView.layout(refreshOri.left, top, refreshOri.right, bottom)
                1
            }
            pullUp -> {
                val top: Int
                val bottom: Int
                when {
                    -offset > mLoadView.measuredHeight -> {
                        mCurState = LOAD_DISPLAY_ALL
                        top = contentView.measuredHeight - mLoadView.measuredHeight
                        bottom = contentView.measuredHeight
                    }
                    else -> {
                        mCurState = LOAD_DISPLAY_HALF
                        top = contentView.measuredHeight + offset
                        bottom = contentView.measuredHeight + mLoadView.measuredHeight + offset
                    }
                }
                curItemView = mLoadView
                mRecycleListener = mLoadListener
                mLoadView.layout(loadOri.left, top, loadOri.right, bottom)
                2
            }
            else -> {
                0
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return super.onTouchEvent(event)
    }


    private fun canPullDown(): Boolean {
        val firstVisiblePosition = (contentView.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
        if (firstVisiblePosition != 0 && contentView.adapter!!.itemCount != 0) {
            return false
        }
        val mostTop = if (contentView.childCount > 0) contentView.getChildAt(0).top else 0
        return mostTop >= 0
    }

    private fun canPullUp(): Boolean {
        linearLayout = contentView.layoutManager as LinearLayoutManager
        val firstVisiblePosition = linearLayout.findFirstVisibleItemPosition()
        val lastVisiblePosition = linearLayout.findLastVisibleItemPosition()
        val lastItemPosition = contentView.adapter!!.itemCount - 1
        if (lastVisiblePosition >= lastItemPosition) {
            val childIndex = lastVisiblePosition - firstVisiblePosition
            val childCount = contentView.childCount
            val index = min(childIndex, childCount - 1)
            val lastVisibleChild = contentView.getChildAt(index)
            if (lastVisibleChild != null) {
                return lastVisibleChild.bottom <= contentView.bottom - contentView.top
            }
        }
        return false
    }


    //----------------  监听接口  -------------------//
    interface RecycleListener {

        //开始拖动-》拖动停止
        fun onDragChange(view: View, state: Int)

        //    拖动释放-》动画停止
        fun onDragAnim(view: View, state: Int)

        //    拖动释放
        fun onDragAnimStart(view: View, state: Int)

        //        刷新itemView开始展示-》展示结束
        fun onAnimDisplay(view: View)

        //        itemView开始移动-》隐藏
        fun onAnimComplete(view: View, state: Int)

    }

    interface OnRefreshStateListener : RecycleListener {
    }

    interface OnLoadStateListener : RecycleListener {
    }

    interface OnRequestDataStateListener {
        fun request(): Boolean
    }

}

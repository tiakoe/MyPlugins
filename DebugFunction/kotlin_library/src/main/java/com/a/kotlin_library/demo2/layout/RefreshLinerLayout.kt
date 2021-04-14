package com.a.kotlin_library.demo2.layout

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.*
import android.widget.AbsListView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main_2.view.*
import kotlin.math.max
import kotlin.math.min

/**
 * 下拉刷新和上拉加载更多的控件
 * 它用于作为可垂直滑动的容器的直接父布局，实现下拉刷新和上拉加载更多的功能。
 */
class RefreshLinerLayout @JvmOverloads constructor(var mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(mContext, attrs, defStyleAttr) {
    protected var mTouchSlop = 0

    /*
    触发下拉刷新的最小高度。
    一般来说，触发下拉刷新的高度就是头部View的高度
     */
    private var mHeaderTriggerMinHeight = 100

    /*
    触发下拉刷新的最大高度。
    一般来说，触发下拉刷新的高度就是头部View的高度
     */
    private var mHeaderTriggerMaxHeight = 400

    /*
     触发上拉加载的最小高度。
     一般来说，触发上拉加载的高度就是尾部View的高度
      */
    private var mFooterTriggerMinHeight = 100

    /*
    触发上拉加载的最大高度。
    一般来说，触发上拉加载的高度就是尾部View的高度
     */
    private var mFooterTriggerMaxHeight = 400

    //头部容器
    private var mHeaderLayout: LinearLayout? = null

    //头部View
    private var mHeaderView: View? = null

    //尾部容器
    private var mFooterLayout: LinearLayout? = null

    //尾部View
    private var mFooterView: View? = null

    //标记 无状态（既不是上拉 也 不是下拉）
    private val STATE_NOT = -1

    //标记 上拉状态
    private val STATE_UP = 1

    //标记 下拉状态
    private val STATE_DOWN = 2

    //当前状态
    private var mCurrentState = STATE_NOT

    //是否处于正在下拉刷新状态
    private var mIsRefreshing = false

    //是否处于正在上拉加载状态
    private var mIsLoadingMore = false

    //是否启用下拉功能（默认开启）
    private var mIsRefresh = true

    /*
    是否启用上拉功能（默认不开启）
    如果设置了上拉加载监听器OnLoadMoreListener，就会自动开启。
     */
    private var mIsLoadMore = false

    //上拉、下拉的阻尼 设置上下拉时的拖动阻力效果
    private var mDamp = 4

    //头部状态监听器
    private var mOnHeaderStateListener: OnHeaderStateListener? = null

    //尾部状态监听器
    private var mOnFooterStateListener: OnFooterStateListener? = null

    //下拉刷新监听器
    private var mOnRefreshListener: OnRefreshListener? = null

    //上拉加载监听器
    private var mOnLoadMoreListener: OnLoadMoreListener? = null

    //是否还有更多数据
    private var mHasMore = true

    //是否显示空布局
    private var mIsEmpty = false

    //滑动到底部，自动触发加载更多
    private var mAutoLoadMore = true

    //是否拦截触摸事件，
    private var mInterceptTouchEvent = false

    //----------------  用于监听手指松开时，屏幕的滑动状态  -------------------//
    //手指松开时，不一定是滑动停止，也有可能是Fling，所以需要监听屏幕滑动的情况。
    // 每隔50毫秒获取一下页面的滑动距离，如果跟上次没有变化，表示滑动停止。
    // 之所以用延时获取滑动距离的方式获取滑动状态，是因为在sdk 23前，无法给所有的View设置OnScrollChangeListener。
    private val SCROLL_DELAY = 0
    private val mScrollHandler = Handler()
    private val mScrollChangeListener: Runnable = object : Runnable {
        override fun run() {
            if (listenScrollChange()) {
                mScrollHandler.postDelayed(this, SCROLL_DELAY.toLong())
            } else {
                mFlingOrientation = ORIENTATION_FLING_NONE
            }
        }
    }
    private var oldOffsetY = 0
    private var mFlingOrientation = 0

    //手指触摸屏幕时的触摸点
    var mTouchX = 0
    var mTouchY = 0
    private fun init() {
        clipToPadding = false
        initHeaderLayout()
        initFooterLayout()
        val configuration = ViewConfiguration.get(mContext)
        mTouchSlop = configuration.scaledTouchSlop
    }

    /**
     * 初始化头部
     */
    private fun initHeaderLayout() {
        mHeaderLayout = LinearLayout(mContext)
        val lp = LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        mHeaderLayout!!.gravity = Gravity.CENTER_HORIZONTAL
        mHeaderLayout!!.layoutParams = lp
        addView(mHeaderLayout)
    }

    /**
     * 设置头部View
     *
     * @param headerView 头部View。这个View必须实现[OnHeaderStateListener]接口。
     */
    fun setHeaderView(headerView: View?) {
        if (headerView is OnHeaderStateListener) {
            mHeaderView = headerView
            mHeaderLayout!!.removeAllViews()
            mHeaderLayout!!.addView(mHeaderView)
            mOnHeaderStateListener = headerView
        } else {
            // headerView必须实现OnHeaderStateListener接口，
            // 并通过OnHeaderStateListener的回调来更新headerView的状态。
            throw IllegalArgumentException("headerView must implement the " +
                    "OnHeaderStateListener")
        }
    }

    /**
     * 初始化尾部
     */
    private fun initFooterLayout() {
        mFooterLayout = LinearLayout(mContext)
        val lp = LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        mFooterLayout!!.gravity = Gravity.CENTER_HORIZONTAL
        mFooterLayout!!.layoutParams = lp
        addView(mFooterLayout)
    }

    /**
     * 设置尾部View
     *
     * @param footerView 尾部View。这个View必须实现[OnFooterStateListener]接口
     */
    fun setFooterView(footerView: View?) {
        if (footerView is OnFooterStateListener) {
            mFooterView = footerView
            mFooterLayout!!.removeAllViews()
            mFooterLayout!!.addView(mFooterView)
            mOnFooterStateListener = footerView
        } else {
            // footerView必须实现OnFooterStateListener接口，
            // 并通过OnFooterStateListener的回调来更新footerView的状态。
            throw IllegalArgumentException("footerView must implement the " +
                    "OnFooterStateListener")
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //测量头部、尾部
        for (index in 0 until 2) {
            measureChild(getChildAt(index), widthMeasureSpec, heightMeasureSpec)
        }
        for (id in 0 until childCount) {
            val view = getChildAt(id)
        }

        //测量内容容器宽高
        val (measureWidth, measureHeight) = measureChild(widthMeasureSpec, heightMeasureSpec)
        val resMeasureWidth = adjustMeasureForMode(widthMeasureSpec, measureWidth)
        val resMeasureHeight = adjustMeasureForMode(heightMeasureSpec, measureHeight)
        //保存测量值
        setMeasuredDimension(resMeasureWidth, resMeasureHeight)
    }

    private fun adjustMeasureForMode(measureSpec: Int, contentSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        return when (specMode) {
            MeasureSpec.EXACTLY -> {
                specSize
            }
            MeasureSpec.AT_MOST -> {
                min(contentSpec, specSize)
            }
            else -> {
                contentSpec
            }
        }
    }

    private fun measureChild(widthMeasureSpec: Int, heightMeasureSpec: Int): Pair<Int, Int> {
        lateinit var view: View
        if (mIsEmpty) {
            //空布局容器
            if (childCount > 3) {
                view = getChildAt(3)
            }
        } else {
            //内容布局容器
            if (childCount > 2) {
                view = getChildAt(2)
            }
        }
        measureChildWithMargins(view, widthMeasureSpec, 0, heightMeasureSpec, 0)
        return measureView(view)
    }

    private fun measureView(view: View): Pair<Int, Int> {
        val lp = view.layoutParams as MarginLayoutParams
        val measureWidth = view.measuredWidth + lp.leftMargin + lp.rightMargin + paddingLeft + paddingRight
        val measureHeight = view.measuredHeight + lp.topMargin + lp.bottomMargin + paddingTop + paddingBottom
        return Pair(measureWidth, measureHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //布局头部
        val headerView = getChildAt(0)
        headerView.layout(paddingLeft, -headerView.measuredHeight,
                paddingLeft + headerView.measuredWidth, 0)

        //布局尾部
        val footerView = getChildAt(1)
        footerView.layout(paddingLeft, measuredHeight,
                paddingLeft + footerView.measuredWidth,
                measuredHeight + footerView.measuredHeight)

        if (mIsEmpty) {
            //空布局容器
            if (childCount > 3) {
                val emptyView = getChildAt(3)
                val emptyLp = emptyView.layoutParams as MarginLayoutParams
                emptyView.layout(paddingLeft + emptyLp.leftMargin,
                        paddingTop + emptyLp.topMargin,
                        paddingLeft + emptyLp.leftMargin + emptyView.measuredWidth,
                        paddingTop + emptyLp.topMargin + emptyView.measuredHeight)
            }
        } else {
            //内容布局容器
            if (childCount > 2) {
                val content = getChildAt(2)
                val contentLp = content.layoutParams as MarginLayoutParams
                content.layout(paddingLeft + contentLp.leftMargin,
                        paddingTop + contentLp.topMargin,
                        paddingLeft + contentLp.leftMargin + content.measuredWidth,
                        paddingTop + contentLp.topMargin + content.measuredHeight)
            }
        }
    }


    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    var oldY = 0
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mCurrentState = STATE_NOT
                mTouchX = ev.x.toInt()
                mTouchY = ev.y.toInt()
                oldY = ev.y.toInt()
                mScrollHandler.removeCallbacksAndMessages(null)
            }
            MotionEvent.ACTION_MOVE -> {
                val newY = ev.y.toInt()
                if (isNestedScroll) {
                    if (canPullDown() && newY > oldY
                            || canPullUp() && newY < oldY) {
                        nestedScroll(oldY - newY)
                    }
                    if (scrollY > 0 && newY > oldY
                            || scrollY < 0 && newY < oldY) {
                        nestedPreScroll(oldY - newY)
                    }
                }
                if (newY > oldY) {
                    mFlingOrientation = ORIENTATION_FLING_UP
                } else if (newY < oldY) {
                    mFlingOrientation = ORIENTATION_FLING_DOWN
                }
                oldY = newY
            }
            MotionEvent.ACTION_UP -> {
                val y = ev.y.toInt()
                val x = ev.x.toInt()
                //是否是点击事件，如果按下和松开的坐标没有改变，就认为是点击事件
                val isClick = Math.abs(mTouchX - x) < mTouchSlop && Math.abs(mTouchY - y) < mTouchSlop
                if (!mInterceptTouchEvent && !isClick && !mIsEmpty) {
                    //监听手指松开时，屏幕的滑动状态
                    //手指松开时，不一定是滑动停止，也有可能是Fling，所以需要监听屏幕滑动的情况。
                    initScrollListen()
                    mScrollHandler.postDelayed(mScrollChangeListener, SCROLL_DELAY.toLong())
                }
                mTouchX = 0
                mTouchY = 0
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val y = ev.y.toInt()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mInterceptTouchEvent = false
                return false
            }
            MotionEvent.ACTION_MOVE -> {
                if (isRefreshingOrLoading) {
                    return false
                }
                if (pullRefresh() && y - mTouchY > mTouchSlop) {
                    return true
                }
                return mHasMore && pullLoadMore() && mTouchY - y > mTouchSlop
            }
            MotionEvent.ACTION_UP -> return false
        }
        return super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_MOVE -> {
                if (mCurrentState == STATE_NOT) {
                    if (pullRefresh() && y - mTouchY > mTouchSlop) {
                        mCurrentState = STATE_DOWN
                        mInterceptTouchEvent = true
                    }
                    if (mHasMore && pullLoadMore() && mTouchY - y > mTouchSlop) {
                        mCurrentState = STATE_UP
                        mInterceptTouchEvent = true
                    }
                }
                if (mTouchY > y) {
                    if (mCurrentState == STATE_UP && !mIsEmpty) {
                        scroll((mTouchY - y) / mDamp, true)
                    }
                } else if (mCurrentState == STATE_DOWN || mIsEmpty) {
                    scroll((mTouchY - y) / mDamp, true)
                }
            }
            MotionEvent.ACTION_UP -> if (!mIsRefreshing && !mIsLoadingMore) {
                val scrollOffset = Math.abs(scrollY)
                if (mCurrentState == STATE_DOWN || mIsEmpty) {
                    if (scrollOffset < headerTriggerHeight) {
                        restore(true)
                    } else {
                        triggerRefresh()
                    }
                } else if (mCurrentState == STATE_UP) {
                    if (scrollOffset < footerTriggerHeight) {
                        restore(true)
                    } else {
                        triggerLoadMore()
                    }
                }
            }
            else -> {
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 是否可下拉刷新。
     *
     * @return 启用了下拉刷新功能，并且可以下拉显示头部，返回true；否则返回false。
     */
    protected fun pullRefresh(): Boolean {
        return mIsRefresh && canPullDown()
    }

    /**
     * 是否可上拉加载更多。
     *
     * @return 启用了上拉加载更多功能，并且有更多数据，并且可以下拉显示头部，返回true，否则返回false。
     */
    protected fun pullLoadMore(): Boolean {
        return mIsLoadMore && mHasMore && canPullUp()
    }

    /**
     * 是否可下拉显示头部。
     *
     * @return 如果是空布局或者内容布局已经滑动到顶部，则返回true，否则返回false。
     */
    protected fun canPullDown(): Boolean {
        if (mIsEmpty) {
            return true
        }
        return if (childCount >= 3) {
            computeVerticalScrollOffset(getChildAt(2)) <= 0
        } else true
    }

    /**
     * 是否可上拉显示尾部。
     *
     * @return 如果不是空布局，并且内容布局已经滑动到底部，则返回true，否则返回false。
     */
    protected fun canPullUp(): Boolean {
        if (mIsEmpty) {
            return false
        }
        if (childCount >= 3) {
            val view = getChildAt(2)
            return (computeVerticalScrollOffset(view) + computeVerticalScrollExtent(view)
                    >= computeVerticalScrollRange(view))
        }
        return false
    }

    /**
     * 是否正在刷新或者正在加载更多
     *
     * @return
     */
    private val isRefreshingOrLoading: Boolean
        get() = mIsRefreshing || mIsLoadingMore

    /**
     * 利用属性动画实现平滑滑动
     *
     * @param start       滑动的开始位置
     * @param end         滑动的结束位置
     * @param duration    滑动的持续时间
     * @param isListening 是否监听滑动变化
     * @param listener
     */
    private fun smoothScroll(start: Int, end: Int, duration: Int, isListening: Boolean,
                             listener: Animator.AnimatorListener?) {
        val animator = ValueAnimator.ofInt(start, end).setDuration(duration.toLong())
        animator.addUpdateListener { animation -> scroll(animation.animatedValue as Int, isListening) }
        if (listener != null) {
            animator.addListener(listener)
        }
        animator.start()
    }

    /**
     * @param y           滑动y的位置
     * @param isListening 是否监听滑动变化，如果为true，滑动的变化将回调到[OnHeaderStateListener]
     * 或[OnFooterStateListener]。
     */
    private fun scroll(y: Int, isListening: Boolean) {
        scrollTo(0, y)
        if (isListening) {
            val scrollOffset = Math.abs(y)
            if (mCurrentState == STATE_DOWN && mOnHeaderStateListener != null) {
                val height = headerTriggerHeight
                mOnHeaderStateListener!!.onScrollChange(mHeaderView, scrollOffset,
                        if (scrollOffset >= height) 100 else scrollOffset * 100 / height)
            }
            if (mCurrentState == STATE_UP && mOnFooterStateListener != null && mHasMore) {
                val height = footerTriggerHeight
                mOnFooterStateListener!!.onScrollChange(mFooterView, scrollOffset,
                        if (scrollOffset >= height) 100 else scrollOffset * 100 / height)
            }
        }
    }

    /**
     * 是否要处理嵌套滑动。
     *
     * @return
     */
    private val isNestedScroll: Boolean
        get() = isRefreshingOrLoading || !mHasMore

    /**
     * 处理嵌套滑动。
     *
     * @param dy 滑动偏移量
     */
    private fun nestedPreScroll(dy: Int) {
        if (mIsRefreshing) {
            val scrollY = scrollY
            if (dy > 0 && scrollY < 0) {
                scrollBy(0, Math.min(dy, -scrollY))
            }
        }
        if (mIsLoadingMore || !mHasMore) {
            val scrollY = scrollY
            if (dy < 0 && scrollY > 0) {
                scrollBy(0, Math.max(dy, -scrollY))
            }
        }
    }

    /**
     * 处理嵌套滑动。
     *
     * @param dy 滑动偏移量
     */
    private fun nestedScroll(dy: Int) {
        if (mIsRefreshing) {
            val height = headerTriggerHeight
            if (dy < 0 && scrollY > -height) { //头部没有完全显示出来
                val offset = -scrollY - height  //剩下的偏移量
                scrollBy(0, max(dy, offset))
            }
        }
        if (mIsLoadingMore || !mHasMore) {
            if (dy > 0 && scrollY < footerTriggerHeight) {
                scrollBy(0, min(dy, height - scrollY))
            }
        }
    }

    /**
     * 手指松开时，监听滑动状态的初始工作
     */
    private fun initScrollListen() {
        if (childCount >= 3) {
            oldOffsetY = computeVerticalScrollOffset(getChildAt(2))
        }
    }

    var scrollVelocity = 0

    /**
     * 监听手指松开时，屏幕的滑动状态
     *
     * @return 返回true表示正在滑动，继续监听；返回false表示滑动停止或者不需要监听。
     */
    private fun listenScrollChange(): Boolean {
        if (childCount >= 3) {
            val offsetY = scrollTopOffset
            val interval = Math.abs(offsetY - oldOffsetY)
            return if (interval > 0) {
                scrollVelocity = interval
                oldOffsetY = offsetY
                if (isNestedScroll) {
                    if (canPullDown() && mFlingOrientation == ORIENTATION_FLING_UP) {
                        nestedScroll(-scrollVelocity)
                    } else if (canPullUp() && mFlingOrientation == ORIENTATION_FLING_DOWN) {
                        nestedScroll(scrollVelocity)
                    }
                    if (scrollY > 0 && mFlingOrientation == ORIENTATION_FLING_UP) {
                        nestedPreScroll(-scrollVelocity)
                    } else if (scrollY < 0 && mFlingOrientation == ORIENTATION_FLING_DOWN) {
                        nestedPreScroll(scrollVelocity)
                    }
                }
                true
            } else {
                // 滑动停止

                //滑动停止时，如果已经滑动到底部，自动触发加载更多
                if (mFlingOrientation == ORIENTATION_FLING_DOWN && mAutoLoadMore && pullLoadMore()) {
                    autoLoadMore()
                    return false
                }
                if (scrollVelocity > 30) {
                    if (canPullDown() && mIsRefreshing && mFlingOrientation == ORIENTATION_FLING_UP) {
                        val height = headerTriggerHeight
                        smoothScroll(scrollY, -height,
                                (1.0f * height * SCROLL_DELAY / scrollVelocity).toInt(),
                                false, null)
                    } else if ((mIsLoadingMore || !mHasMore) && canPullUp()
                            && mFlingOrientation == ORIENTATION_FLING_DOWN) {
                        val height = footerTriggerHeight
                        smoothScroll(scrollY, height,
                                (1.0f * height * SCROLL_DELAY / scrollVelocity).toInt(),
                                false, null)
                    }
                }
                scrollVelocity = 0
                false
            }
        }
        return false
    }

    /**
     * 获取内容布局滑动到顶部的偏移量
     *
     * @return
     */
    private val scrollTopOffset: Int
        get() {
            if (childCount >= 3) {
                val view = getChildAt(2)
                return computeVerticalScrollOffset(view)
            }
            return 0
        }

    /**
     * 获取内容布局滑动到底部部的偏移量
     *
     * @return
     */
    private val scrollBottomOffset: Int
        private get() {
            if (childCount >= 3) {
                val view = getChildAt(2)
                return (computeVerticalScrollRange(view) - computeVerticalScrollOffset(view)
                        - computeVerticalScrollExtent(view))
            }
            return 0
        }

    private fun computeVerticalScrollOffset(view: View): Int {
        try {
            view.computeScroll()
            val method = View::class.java.getDeclaredMethod("computeVerticalScrollOffset")
            method.isAccessible = true
            return method.invoke(view) as Int
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return view.scrollY
    }

    private fun computeVerticalScrollRange(view: View): Int {
        try {
            val method = View::class.java.getDeclaredMethod("computeVerticalScrollRange")
            method.isAccessible = true
            return method.invoke(view) as Int
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return view.height
    }

    private fun computeVerticalScrollExtent(view: View): Int {
        try {
            val method = View::class.java.getDeclaredMethod("computeVerticalScrollExtent")
            method.isAccessible = true
            return method.invoke(view) as Int
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return view.height
    }

    /**
     * @param isRefresh 是否开启下拉刷新功能 默认开启
     */
    fun setRefreshEnable(isRefresh: Boolean) {
        mIsRefresh = isRefresh
    }

    /**
     * @param isLoadMore 是否开启上拉功能 默认不开启
     */
    fun setLoadMoreEnable(isLoadMore: Boolean) {
        mIsLoadMore = isLoadMore
    }

    /**
     * 还原
     */
    private fun restore(isListener: Boolean) {
        smoothScroll(scrollY, 0, 200, isListener, null)
    }

    /**
     * 通知刷新完成。它会回调[OnHeaderStateListener.onRetract]方法
     *
     * @param isSuccess 是否刷新成功
     */
    fun finishRefresh(isSuccess: Boolean) {
        if (mIsRefreshing) {
            mCurrentState = STATE_NOT
            if (mOnHeaderStateListener != null) {
                mOnHeaderStateListener!!.onRetract(mHeaderView, isSuccess)
            }
        }
        postDelayed({ //平滑收起头部。
            smoothScroll(scrollY, 0, 200, false, null)
            mIsRefreshing = false
        }, 500)
    }

    /**
     * 通知加载更多完成。它会回调[OnFooterStateListener.onRetract]方法
     * 请使用[.finishLoadMore]
     */
    @Deprecated("") //不推荐使用这个方法 因为同时调用它和hasMore(boolean)两个方法时，尾部无法显示“加载完成”的提示。推荐使用finishLoadMore(boolean hasMore);
    fun finishLoadMore() {
        //为了处理先调用finishLoadMore()，后调用hasMore(boolean)的情况;延时调用finishLoadMore(mHasMore);
        postDelayed({ finishLoadMore(true, mHasMore) }, 0)
    }

    /**
     * 通知加载更多完成。它会回调[OnFooterStateListener.onRetract]方法
     *
     * @param isSuccess 是否加载成功
     * @param hasMore   是否还有更多数据
     */
    fun finishLoadMore(isSuccess: Boolean, hasMore: Boolean) {
        if (mIsLoadingMore) {
            mCurrentState = STATE_NOT
            if (mOnFooterStateListener != null) {
                mOnFooterStateListener!!.onRetract(mFooterView, isSuccess)
            }
        }
        // 处理尾部的收起。
        postDelayed({
            mIsLoadingMore = false
            hasMore(hasMore)
            if (scrollBottomOffset > 0) {
                // 如果有新的内容加载出来，就收起尾部，并把新内容显示出来。。
                if (childCount >= 3) {
                    val v = getChildAt(2)
                    if (v is AbsListView) {
                        v.smoothScrollBy(scrollY, 0)
                    } else {
                        v.scrollBy(0, scrollY)
                    }
                }
                scroll(0, false)
            } else if (mHasMore) {
                smoothScroll(scrollY, 0, 200, false, null)
            }
        }, 500)
    }

    /**
     * 自动触发下拉刷新。只有启用了下拉刷新功能时起作用。
     */
    fun autoRefresh() {
        if (!mIsRefresh || isRefreshingOrLoading) {
            return
        }
        post {
            mCurrentState = STATE_DOWN
            smoothScroll(scrollY, -headerTriggerHeight, 200, true,
                    object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            triggerRefresh()
                        }
                    })
        }
    }

    /**
     * 触发下拉刷新
     */
    private fun triggerRefresh() {
        if (!mIsRefresh || isRefreshingOrLoading) {
            return
        }
        mIsRefreshing = true
        mCurrentState = STATE_NOT
        scroll(-headerTriggerHeight, false)
        if (mOnHeaderStateListener != null) {
            mOnHeaderStateListener!!.onRefresh(mHeaderView)
        }
        if (mOnRefreshListener != null) {
            mOnRefreshListener!!.onRefresh()
        }
    }

    /**
     * 自动触发上拉加载更多。只有在启用了上拉加载更多功能并且有更多数据时起作用。
     */
    fun autoLoadMore() {
        if (isRefreshingOrLoading || !mHasMore || !mIsLoadMore || mIsEmpty) {
            return
        }
        post {
            mCurrentState = STATE_UP
            smoothScroll(scrollY, footerTriggerHeight, 200, true,
                    object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            triggerLoadMore()
                        }
                    })
        }
    }

    /**
     * 触发上拉加载更多。
     */
    private fun triggerLoadMore() {
        if (isRefreshingOrLoading || !mHasMore || !mIsLoadMore || mIsEmpty) {
            return
        }
        mIsLoadingMore = true
        mCurrentState = STATE_NOT
        scroll(footerTriggerHeight, false)
        if (mOnFooterStateListener != null) {
            mOnFooterStateListener!!.onRefresh(mFooterView)
        }
        if (mOnLoadMoreListener != null) {
            mOnLoadMoreListener!!.onLoadMore()
        }
    }

    /**
     * 是否自动触发加载更多。只有在启用了上拉加载更多功能时起作用。
     *
     * @param autoLoadMore 如果为true，滑动到底部，自动触发加载更多
     */
    fun setAutoLoadMore(autoLoadMore: Boolean) {
        mAutoLoadMore = autoLoadMore
    }

    /**
     * 是否还有更多数据，只有为true是才能上拉加载更多.
     * 它会回调[OnFooterStateListener.hasMore]方法。
     *
     * @param hasMore 默认为true。
     */
    fun hasMore(hasMore: Boolean) {
        if (mHasMore != hasMore) {
            mHasMore = hasMore
            if (mOnFooterStateListener != null) {
                mOnFooterStateListener!!.onHasMore(mFooterView, hasMore)
            }
        }
    }

    /**
     * 获取触发下拉刷新的下拉高度
     *
     * @return
     */
    val headerTriggerHeight: Int
        get() {
            var height = mHeaderLayout!!.height
            height = Math.max(height, mHeaderTriggerMinHeight)
            height = Math.min(height, mHeaderTriggerMaxHeight)
            return height
        }

    /**
     * 获取触发上拉加载的上拉高度
     *
     * @return
     */
    val footerTriggerHeight: Int
        get() {
            var height = mFooterLayout!!.height
            height = Math.max(height, mFooterTriggerMinHeight)
            height = Math.min(height, mFooterTriggerMaxHeight)
            return height
        }

    /**
     * 设置触发下拉刷新的最小高度。
     *
     * @param headerTriggerMinHeight
     */
    fun setHeaderTriggerMinHeight(headerTriggerMinHeight: Int) {
        mHeaderTriggerMinHeight = headerTriggerMinHeight
    }

    /**
     * 设置触发下拉刷新的最大高度。
     *
     * @param headerTriggerMaxHeight
     */
    fun setHeaderTriggerMaxHeight(headerTriggerMaxHeight: Int) {
        mHeaderTriggerMaxHeight = headerTriggerMaxHeight
    }

    /**
     * 设置触发上拉加载的最小高度。
     *
     * @param footerTriggerMinHeight
     */
    fun setFooterTriggerMinHeight(footerTriggerMinHeight: Int) {
        mFooterTriggerMinHeight = footerTriggerMinHeight
    }

    /**
     * 设置触发上拉加载的最大高度。
     *
     * @param footerTriggerMaxHeight
     */
    fun setFooterTriggerMaxHeight(footerTriggerMaxHeight: Int) {
        mFooterTriggerMaxHeight = footerTriggerMaxHeight
    }

    /**
     * 设置拉动阻力 （1到10）
     *
     * @param damp
     */
    fun setDamp(damp: Int) {
        mDamp = if (damp < 1) {
            1
        } else if (damp > 10) {
            10
        } else {
            damp
        }
    }

    /**
     * 隐藏内容布局，显示空布局
     */
    fun showEmpty() {
        if (!mIsEmpty) {
            mIsEmpty = true
            //显示空布局
            if (childCount > 3) {
                getChildAt(3).visibility = VISIBLE
            }
            //隐藏内容布局
            if (childCount > 2) {
                getChildAt(2).visibility = GONE
            }
        }
    }

    /**
     * 隐藏空布局，显示内容布局
     */
    fun hideEmpty() {
        if (mIsEmpty) {
            mIsEmpty = false
            //隐藏空布局
            if (childCount > 3) {
                getChildAt(3).visibility = GONE
            }
            //显示内容布局
            if (childCount > 2) {
                getChildAt(2).visibility = VISIBLE
            }
        }
    }

    /**
     * 设置加载更多的监听，触发加载时回调。
     * RefreshLayout默认没有启用上拉加载更多的功能，如果设置了OnLoadMoreListener，则自动启用。
     *
     * @param listener
     */
    fun setOnLoadMoreListener(listener: OnLoadMoreListener?) {
        mOnLoadMoreListener = listener
        if (listener != null) {
            setLoadMoreEnable(true)
        }
    }

    /**
     * 设置刷新监听，触发刷新时回调
     *
     * @param listener
     */
    fun setOnRefreshListener(listener: OnRefreshListener?) {
        mOnRefreshListener = listener
    }
    //----------------  监听接口  -------------------//
    /**
     * 头部状态监听器
     */
    interface OnHeaderStateListener {
        /**
         * 头部滑动变化
         *
         * @param headerView   头部View
         * @param scrollOffset 滑动距离
         * @param scrollRatio  从开始到触发阀值的滑动比率（0到100）如果滑动到达了阀值，就算再滑动，这个值也是100
         */
        fun onScrollChange(headerView: View?, scrollOffset: Int, scrollRatio: Int)

        /**
         * 头部处于刷新状态 （触发下拉刷新的时候调用）
         *
         * @param headerView 头部View
         */
        fun onRefresh(headerView: View?)

        /**
         * 刷新完成，头部收起
         *
         * @param headerView 头部View
         * @param isSuccess  是否刷新成功
         */
        fun onRetract(headerView: View?, isSuccess: Boolean)
    }

    /**
     * 尾部状态监听器
     */
    interface OnFooterStateListener {
        /**
         * 尾部滑动变化
         *
         * @param footerView   尾部View
         * @param scrollOffset 滑动距离
         * @param scrollRatio  从开始到触发阀值的滑动比率（0到100）如果滑动到达了阀值，就算在滑动，这个值也是100
         */
        fun onScrollChange(footerView: View?, scrollOffset: Int, scrollRatio: Int)

        /**
         * 尾部处于加载状态 （触发上拉加载的时候调用）
         *
         * @param footerView 尾部View
         */
        fun onRefresh(footerView: View?)

        /**
         * 加载完成，尾部收起
         *
         * @param footerView 尾部View
         * @param isSuccess  是否加载成功
         */
        fun onRetract(footerView: View?, isSuccess: Boolean)

        /**
         * 是否还有更多(是否可以加载下一页)
         *
         * @param footerView
         * @param hasMore
         */
        fun onHasMore(footerView: View?, hasMore: Boolean)
    }

    /**
     * 上拉加载监听器
     */
    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    /**
     * 下拉更新监听器
     */
    interface OnRefreshListener {
        fun onRefresh()
    }

    companion object {
        private const val ORIENTATION_FLING_NONE = 0
        private const val ORIENTATION_FLING_UP = 1
        private const val ORIENTATION_FLING_DOWN = 2
    }

    init {
        init()
    }
}

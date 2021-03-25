package com.a.kotlin_library.demo2.fragment

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.a.kotlin_library.demo2.fragment.home.HomeFragment
import com.a.kotlin_library.demo2.fragment.my.MyFragment
import com.a.kotlin_library.demo2.fragment.project.ProjectFragment
import com.a.kotlin_library.demo2.fragment.public.PublicFragment
import com.a.kotlin_library.demo2.fragment.square.SquareFragment
import com.a.kotlin_library.demo2.loadCallBack.EmptyCallback
import com.a.kotlin_library.demo2.loadCallBack.LoadingCallback
import com.a.kotlin_library.demo2.utils.SettingUtil
import com.a.kotlin_library.demo2.utils.appContext
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir


fun ViewPager2.initMain(fragment: Fragment): ViewPager2 {
    this.isUserInputEnabled = false
    this.offscreenPageLimit = 5
    adapter = object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return HomeFragment()
                }
                1 -> {
                    return ProjectFragment()
                }
                2 -> {
                    return SquareFragment()
                }
                3 -> {
                    return PublicFragment()
                }
                4 -> {
                    return MyFragment()
                }
                else -> {
                    return HomeFragment()
                }
            }
        }

        override fun getItemCount() = 5
    }
    return this
}

// 拦截BottomNavigation长按事件 防止长按时出现Toast
fun BottomNavigationViewEx.interceptLongClick(vararg ids: Int) {
    val bottomMenuView = (this.getChildAt(0) as ViewGroup)
    for (index in ids.indices) {
        bottomMenuView.getChildAt(index).findViewById<View>(ids[index]).setOnLongClickListener {
            true
        }
    }
}

fun BottomNavigationViewEx.init(navigationItemSelectedAction: (Int) -> Unit): BottomNavigationViewEx {
    enableAnimation(true)
    itemIconTintList = SettingUtil.getColorStateList(SettingUtil.getColor(appContext))
    itemTextColor = SettingUtil.getColorStateList(appContext)
    setTextSize(12F)
    setOnNavigationItemSelectedListener { it ->
        navigationItemSelectedAction.invoke(it.itemId)
        true
    }
    return this
}

fun ViewPager2.init(
        fragment: Fragment,
        fragments: ArrayList<Fragment>,
        isUserInputEnabled: Boolean = true
): ViewPager2 {
    //是否可滑动
    this.isUserInputEnabled = isUserInputEnabled
    //设置适配器
    adapter = object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int) = fragments[position]
        override fun getItemCount() = fragments.size
    }
    return this
}


//fun MagicIndicator.bindViewPager2(
//        viewPager: ViewPager2,
//        mStringList: List<String> = arrayListOf(),
//        action: (index: Int) -> Unit = {}) {
//    val commonNavigator = CommonNavigator(appContext)
//    commonNavigator.adapter = object : CommonNavigatorAdapter() {
//
//        override fun getCount(): Int {
//            return mStringList.size
//        }
//
//        override fun getTitleView(context: Context, index: Int): IPagerTitleView {
//            return ScaleTransitionPagerTitleView(appContext).apply {
//                //设置文本
//                text = mStringList[index].toHtml()
//                //字体大小
//                textSize = 17f
//                //未选中颜色
//                normalColor = Color.WHITE
//                //选中颜色
//                selectedColor = Color.WHITE
//                //点击事件
//                setOnClickListener {
//                    viewPager.currentItem = index
//                    action.invoke(index)
//                }
//            }
//        }
//
//        override fun getIndicator(context: Context): IPagerIndicator {
//            return LinePagerIndicator(context).apply {
//                mode = LinePagerIndicator.MODE_EXACTLY
//                //线条的宽高度
//                lineHeight = UIUtil.dip2px(appContext, 3.0).toFloat()
//                lineWidth = UIUtil.dip2px(appContext, 30.0).toFloat()
//                //线条的圆角
//                roundRadius = UIUtil.dip2px(appContext, 6.0).toFloat()
//                startInterpolator = AccelerateInterpolator()
//                endInterpolator = DecelerateInterpolator(2.0f)
//                //线条的颜色
//                setColors(Color.WHITE)
//            }
//        }
//    }
//    this.navigator = commonNavigator
//
//    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//        override fun onPageSelected(position: Int) {
//            super.onPageSelected(position)
//            this@bindViewPager2.onPageSelected(position)
//            action.invoke(position)
//        }
//
//        override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//        ) {
//            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//            this@bindViewPager2.onPageScrolled(position, positionOffset, positionOffsetPixels)
//        }
//
//        override fun onPageScrollStateChanged(state: Int) {
//            super.onPageScrollStateChanged(state)
//            this@bindViewPager2.onPageScrollStateChanged(state)
//        }
//    })
//}
//
//
///**
// * 初始化有返回键的toolbar
// */
//fun Toolbar.initClose(
//        titleStr: String = "",
//        backImg: Int = R.drawable.ic_back,
//        onBack: (toolbar: Toolbar) -> Unit
//): Toolbar {
//    setBackgroundColor(SettingUtil.getColor(appContext))
//    title = titleStr.toHtml()
//    setNavigationIcon(backImg)
//    setNavigationOnClickListener { onBack.invoke(this) }
//    return this
//}


//fun LoadService<*>.setErrorText(message: String) {
//    if (message.isNotEmpty()) {
//        this.setCallBack(ErrorCallback::class.java) { _, view ->
//            view.findViewById<TextView>(R.id.error_text).text = message
//        }
//    }
//}

/**
 * 设置错误布局
 * @param message 错误布局显示的提示内容
 */
//fun LoadService<*>.showError(message: String = "") {
//    this.setErrorText(message)
//    this.showCallback(ErrorCallback::class.java)
//}

/**
 * 设置空布局
 */
fun LoadService<*>.showEmpty() {
    this.showCallback(EmptyCallback::class.java)
}

/**
 * 设置加载中
 */
fun LoadService<*>.showLoading() {
    this.showCallback(LoadingCallback::class.java)
}

fun loadServiceInit(view: View, callback: () -> Unit): LoadService<Any> {
    val loadsir = LoadSir.getDefault().register(view) {
        //点击重试时触发的操作
        callback.invoke()
    }
    loadsir.showSuccess()
    SettingUtil.setLoadingColor(SettingUtil.getColor(appContext), loadsir)
    return loadsir
}

//绑定普通的Recyclerview
fun RecyclerView.init(
        layoutManger: RecyclerView.LayoutManager,
        bindAdapter: RecyclerView.Adapter<*>,
        isScroll: Boolean = true
): RecyclerView {
    layoutManager = layoutManger
    setHasFixedSize(true)
    adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}

fun RecyclerView.initFloatBtn(floatbtn: FloatingActionButton) {
    //监听recyclerview滑动到顶部的时候，需要把向上返回顶部的按钮隐藏
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        @SuppressLint("RestrictedApi")
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!canScrollVertically(-1)) {
                floatbtn.visibility = View.INVISIBLE
            }
        }
    })
    floatbtn.backgroundTintList = SettingUtil.getOneColorStateList(appContext)
    floatbtn.setOnClickListener {
        val layoutManager = layoutManager as LinearLayoutManager
        //如果当前recyclerview 最后一个视图位置的索引大于等于40，则迅速返回顶部，否则带有滚动动画效果返回到顶部
        if (layoutManager.findLastVisibleItemPosition() >= 40) {
            scrollToPosition(0)//没有动画迅速返回到顶部(马上)
        } else {
            smoothScrollToPosition(0)//有滚动动画返回到顶部(有点慢)
        }
    }
}


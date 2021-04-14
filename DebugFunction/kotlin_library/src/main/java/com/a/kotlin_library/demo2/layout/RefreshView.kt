package com.a.kotlin_library.demo2.layout

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.a.kotlin_library.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 下拉刷新头部View
 * 头部View必须实现RefreshLayout.OnHeaderStateListener，并通过这个接口的回调来更新头部View的状态。
 */
class RefreshView(context: Context) : RelativeLayout(context), RefreshLinerLayout.OnHeaderStateListener {
    private var ivLoading: ImageView? = null
    private var tvState: TextView? = null
    private var tvRefreshTime: TextView? = null
    private val animationDrawable: AnimationDrawable
    private var mLastUpdateFormat: DateFormat? = null
    private var headerPulling: String? = null
    private var headerRefreshing: String? = null
    private var headerRelease: String? = null
    private var headerRefreshFinish: String? = null
    private var headerRefreshFailure: String? = null
    private var headerUpdate: String? = null

    private fun init(context: Context) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.header_view_layout, this, false)
        this.addView(layout, LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT))
        initView(layout)
        val resources = context.resources
        headerPulling = resources.getString(R.string.header_pulling)
        headerRefreshing = resources.getString(R.string.header_refreshing)
        headerRelease = resources.getString(R.string.header_release)
        headerRefreshFinish = resources.getString(R.string.header_refresh_finish)
        headerRefreshFailure = resources.getString(R.string.header_refresh_failure)
        headerUpdate = resources.getString(R.string.header_update)
        mLastUpdateFormat = SimpleDateFormat(headerUpdate, Locale.getDefault())
        tvRefreshTime!!.text = (mLastUpdateFormat as SimpleDateFormat).format(Date())
    }

    private fun initView(view: View) {
        ivLoading = view.findViewById<View>(R.id.iv_loading) as ImageView
        tvState = view.findViewById<View>(R.id.tv_state) as TextView
        tvRefreshTime = view.findViewById<View>(R.id.tv_refresh_time) as TextView
    }

    fun setRefreshTime(date: Date?) {
        tvRefreshTime!!.text = mLastUpdateFormat!!.format(date)
    }

    override fun onScrollChange(head: View?, scrollOffset: Int, scrollRatio: Int) {
        if (scrollRatio < 100) {
            tvState!!.text = headerPulling
            ivLoading!!.setImageResource(R.drawable.icon_down_arrow)
            ivLoading!!.rotation = 0f
        } else {
            tvState!!.text = headerRelease
            ivLoading!!.setImageResource(R.drawable.icon_down_arrow)
            ivLoading!!.rotation = 180f
        }
    }

    override fun onRefresh(headerView: View?) {
        tvState!!.text = headerRefreshing
        ivLoading!!.setImageDrawable(animationDrawable)
        animationDrawable.start()
    }

    override fun onRetract(headerView: View?, isSuccess: Boolean) {
        if (isSuccess) {
            tvState!!.text = headerRefreshFinish
            tvRefreshTime!!.text = mLastUpdateFormat!!.format(Date())
        } else {
            tvState!!.text = headerRefreshFailure
        }
        ivLoading!!.setImageBitmap(null)
    }

    init {
        animationDrawable = resources.getDrawable(R.drawable.progress_round) as AnimationDrawable
        init(context)
    }
}

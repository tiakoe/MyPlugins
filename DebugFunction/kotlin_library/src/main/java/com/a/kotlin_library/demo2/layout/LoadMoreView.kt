package com.a.kotlin_library.demo2.layout

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.a.kotlin_library.R

/**
 * 下拉刷新尾部View
 * 尾部View必须实现RefreshLayout.OnFooterStateListener，并通过这个接口的回调来更新尾部View的状态。
 */
class LoadMoreView(context: Context) : RelativeLayout(context), RefreshLinerLayout.OnFooterStateListener {
    private var ivLoading: ImageView? = null
    private var tvState: TextView? = null
    private val animationDrawable: AnimationDrawable
    private var hasMore = true
    private var footerPulling: String? = null
    private var footerRelease: String? = null
    private var footerLoading: String? = null
    private var footerLoadingFinish: String? = null
    private var footerLoadingFailure: String? = null
    private var footerNothing: String? = null
    private fun init(context: Context) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.footer_view_layout, this, false)
        this.addView(layout, LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT))
        initView(layout)
        val resources = context.resources
        footerPulling = resources.getString(R.string.footer_pulling)
        footerRelease = resources.getString(R.string.footer_release)
        footerLoading = resources.getString(R.string.footer_loading)
        footerLoadingFinish = resources.getString(R.string.footer_loading_finish)
        footerLoadingFailure = resources.getString(R.string.footer_loading_failure)
        footerNothing = resources.getString(R.string.footer_nothing)
    }

    private fun initView(view: View) {
        ivLoading = view.findViewById<View>(R.id.iv_loading) as ImageView
        tvState = view.findViewById<View>(R.id.tv_state) as TextView
    }

    override fun onScrollChange(tail: View?, scrollOffset: Int, scrollRatio: Int) {
        if (hasMore) {
            if (scrollRatio < 100) {
                tvState!!.text = footerPulling
                ivLoading!!.setImageResource(R.drawable.icon_down_arrow)
                ivLoading!!.rotation = 180f
            } else {
                tvState!!.text = footerRelease
                ivLoading!!.setImageResource(R.drawable.icon_down_arrow)
                ivLoading!!.rotation = 0f
            }
        }
    }

    override fun onRefresh(footerView: View?) {
        if (hasMore) {
            tvState!!.text = footerLoading
            ivLoading!!.setImageDrawable(animationDrawable)
            animationDrawable.start()
        }
    }

    override fun onRetract(footerView: View?, isSuccess: Boolean) {
        if (hasMore) {
            tvState!!.text = if (isSuccess) footerLoadingFinish else footerLoadingFailure
            ivLoading!!.setImageBitmap(null)
        }
    }

    override fun onHasMore(tail: View?, hasMore: Boolean) {
        this.hasMore = hasMore
        if (!hasMore) {
            tvState!!.text = footerNothing
            ivLoading!!.setImageBitmap(null)
        }
    }

    init {
        animationDrawable = resources.getDrawable(R.drawable.progress_round) as AnimationDrawable
        init(context)
    }
}

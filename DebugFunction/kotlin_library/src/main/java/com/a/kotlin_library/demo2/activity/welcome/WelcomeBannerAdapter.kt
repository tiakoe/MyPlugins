package com.a.kotlin_library.demo2.activity.welcome


import android.view.View
import com.a.kotlin_library.R
import com.a.kotlin_library.demo2.widget.banner.WelcomeBannerViewHolder
import com.zhpan.bannerview.BaseBannerAdapter

class WelcomeBannerAdapter : BaseBannerAdapter<String, WelcomeBannerViewHolder>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.banner_item_welcome
    }

    override fun createViewHolder(itemView: View, viewType: Int): WelcomeBannerViewHolder {
        return WelcomeBannerViewHolder(itemView);
    }

    override fun onBind(
            holder: WelcomeBannerViewHolder?,
            data: String?,
            position: Int,
            pageSize: Int
    ) {
        holder?.bindData(data, position, pageSize);
    }
}

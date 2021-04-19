package com.a.kotlin_library.demo2.fragment.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.a.kotlin_library.R
import com.a.kotlin_library.demo2.activity.other2.HomeChildActivity
import com.a.kotlin_library.demo2.utils.kLog
import com.a.kotlin_library.room.table.BannerTable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.youth.banner.adapter.BannerAdapter

class HomeBannerAdapter internal constructor(private val context: Context, mDatas: List<BannerTable?>?) : BannerAdapter<BannerTable?, HomeBannerAdapter.BannerViewHolder?>(mDatas) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView)
    }

    override fun onBindView(holder: BannerViewHolder?, data: BannerTable?, position: Int, size: Int) {
        if (data != null) {
            holder?.bind(context, data, position)
        }
    }

    inner class BannerViewHolder(@NonNull view: ImageView) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view
        fun bind(context: Context, bannerTable: BannerTable, position: Int) {
            kLog(401170859, bannerTable.url)
            Glide.with(context)
                    .load(bannerTable.imagePath)
                    .error(R.drawable.ic_baseline_error_24)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                    .into(imageView)

            val intent = Intent(context, HomeChildActivity::class.java)
            val bundle = Bundle()
            bundle.putString("url", bannerTable.url)
            intent.putExtras(bundle)
            imageView.setOnClickListener {
                context.startActivity(intent)
            }
        }
    }
}

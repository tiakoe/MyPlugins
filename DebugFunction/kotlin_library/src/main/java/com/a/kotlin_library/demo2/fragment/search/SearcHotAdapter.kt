package com.a.kotlin_library.demo2.fragment.search

import android.graphics.Color
import com.a.kotlin_library.R
import com.a.kotlin_library.demo2.bean.home.SearchResponse
import com.a.kotlin_library.demo2.utils.SettingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.util.*

class SearcHotAdapter(data: ArrayList<SearchResponse>) :
        BaseQuickAdapter<SearchResponse, BaseViewHolder>(R.layout.flow_layout, data) {

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: SearchResponse) {
        holder.setText(R.id.flow_tag, item.name)
        holder.setTextColor(R.id.flow_tag, randomColor())
    }

}

//设置适配器的列表动画
fun BaseQuickAdapter<*, *>.setAdapterAnimation(mode: Int) {
    //等于0，关闭列表动画 否则开启
    if (mode == 0) {
        this.animationEnable = false
    } else {
        this.animationEnable = true
        this.setAnimationWithDefault(BaseQuickAdapter.AnimationType.values()[mode - 1])
    }
}

fun randomColor(): Int {
    Random().run {
        //0-190, 如果颜色值过大,就越接近白色,就看不清了,所以需要限定范围
        val red = nextInt(190)
        val green = nextInt(190)
        val blue = nextInt(190)
        //使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
        return Color.rgb(red, green, blue)
    }
}

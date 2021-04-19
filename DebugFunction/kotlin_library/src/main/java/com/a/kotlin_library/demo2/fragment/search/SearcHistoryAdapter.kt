package com.a.kotlin_library.demo2.fragment.search

import com.a.kotlin_library.R
import com.a.kotlin_library.demo2.utils.SettingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class SearcHistoryAdapter(data: MutableList<String>) :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_history, data) {

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.item_history_text, item)
    }

}

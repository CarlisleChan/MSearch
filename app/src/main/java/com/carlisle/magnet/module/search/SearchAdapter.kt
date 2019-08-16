package com.carlisle.magnet.module.search

import com.carlisle.magnet.R
import com.carlisle.magnet.provider.http.MagnetResource
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SearchAdapter : BaseQuickAdapter<MagnetResource, BaseViewHolder>(R.layout.item_search) {

    override fun convert(helper: BaseViewHolder, item: MagnetResource) {
        helper.setText(R.id.tv_index, "${helper.layoutPosition + 1}")
        helper.setText(R.id.tv_title, item.name)
        helper.setText(R.id.tv_datetime, "发布时间: ${item.date}")
        helper.setText(R.id.tv_size, "大小: ${item.formatSize}")
        helper.setText(R.id.tv_hot, "人气: ${item.hot ?: ""}")

        helper.addOnClickListener(R.id.ll_root_view)
        helper.addOnClickListener(R.id.iv_more)
    }

}
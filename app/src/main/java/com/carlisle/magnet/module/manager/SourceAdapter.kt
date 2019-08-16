package com.carlisle.magnet.module.manager

import android.widget.CheckBox
import com.carlisle.magnet.R
import com.carlisle.magnet.provider.http.MagnetRule
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SourceAdapter(var selected: MutableMap<String, MagnetRule>) : BaseQuickAdapter<MagnetRule, BaseViewHolder>(R.layout.item_source) {

    override fun convert(helper: BaseViewHolder, item: MagnetRule) {
        helper.setText(R.id.tv_title, item.site)
        helper.setChecked(R.id.cb_status, selected.containsKey(item.site))

        helper.getView<CheckBox>(R.id.cb_status).setOnClickListener {
            if ((it as CheckBox).isChecked) {
                selected[item.site] = item
            } else {
                selected.remove(item.site)
            }
        }
    }

}

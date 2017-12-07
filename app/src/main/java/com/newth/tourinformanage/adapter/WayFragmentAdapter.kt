package com.newth.tourinformanage.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.newth.tourinformanage.R
import com.newth.tourinformanage.bean.WayInfo

/**
 * Created by Mr.chen on 2017/12/7.
 */

class WayFragmentAdapter(data: List<WayInfo>?) : BaseQuickAdapter<WayInfo, BaseViewHolder>(R.layout.item_wayinfo, data) {

    override fun convert(helper: BaseViewHolder, item: WayInfo) {
        helper.setText(R.id.text_way_name,item.wayName)
                .setText(R.id.text_way_content,item.wayContent)
                .setText(R.id.text_way_kind,item.wayKind)
                .setText(R.id.text_location_show,item.wayLocation)
    }
}

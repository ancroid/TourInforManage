package com.newth.tourinformanage.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.newth.tourinformanage.R
import com.newth.tourinformanage.bean.IPickerViewData

/**
 * Created by Mr.chen on 2017/12/1.
 */

class LocationAdapter(data: List<IPickerViewData>?) : BaseQuickAdapter<IPickerViewData, BaseViewHolder>(R.layout.item_location, data) {

    override fun convert(helper: BaseViewHolder, item: IPickerViewData) {
        helper.setText(R.id.tv_location_name, item.getPickerViewText())
    }
}

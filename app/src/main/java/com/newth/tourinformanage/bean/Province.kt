package com.newth.tourinformanage.bean



/**
 * Created by Mr.chen on 2017/11/28.
 */
class Province:IPickerViewData {
    override fun getTargetID(): Int? {
        return provinceCode
    }

    override fun getPickerViewText(): String? {
        return provinceName
    }

    var id: Int? = null
    var provinceName: String? = null
    var provinceCode: Int? = null
}
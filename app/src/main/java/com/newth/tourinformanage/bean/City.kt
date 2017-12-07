package com.newth.tourinformanage.bean


/**
 * Created by Mr.chen on 2017/11/28.
 */
class City :IPickerViewData{
    override fun getTargetID(): Int? {
        return cityCode
    }

    override fun getPickerViewText(): String? {
        return cityName
    }

    var id: Int? = null
    var provinceID: Int? = null
    var cityName: String? = null
    var cityCode: Int? = null
}
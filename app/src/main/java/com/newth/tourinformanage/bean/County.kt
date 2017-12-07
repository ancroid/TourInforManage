package com.newth.tourinformanage.bean


/**
 * Created by Mr.chen on 2017/11/28.
 */
class County :IPickerViewData{


    var id: Int? = null
    var cityID: Int? = null
    var countyName: String? = null

    override fun getPickerViewText(): String? {
        return countyName
    }

    override fun getTargetID(): Int? {
        return 1
    }
}
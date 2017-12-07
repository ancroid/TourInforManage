package com.newth.tourinformanage.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by Mr.chen on 2017/12/7.
 */

class WayMutipleItem : MultiItemEntity {
    private var itemType: Int = 0
    var wayInfo: WayInfo? = null
    var point: WayInfo.Point? = null

    constructor(itemType: Int, wayInfo: WayInfo, point: WayInfo.Point) {
        this.itemType = itemType
        this.wayInfo = wayInfo
        this.point = point
    }

    constructor(itemType: Int, wayInfo: WayInfo) {
        this.itemType = itemType
        this.wayInfo = wayInfo
    }

    constructor(itemType: Int, point: WayInfo.Point) {
        this.itemType = itemType
        this.point = point
    }

    override fun getItemType(): Int {
        return itemType
    }

    companion object {
        val ONE = 1
        val TWO = 2
    }
}

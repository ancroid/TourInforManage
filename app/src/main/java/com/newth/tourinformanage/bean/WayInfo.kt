package com.newth.tourinformanage.bean


/**
 * Created by Mr.chen on 2017/12/4.
 */
class WayInfo {
    var id: Int? = null
    var wayName: String? = null
    var wayLocation: String? = null
    var wayKind: String? = null
    var wayContent: String? = null
    var wayPointList: ArrayList<Point>? = null

    class Point {
        var id: Int? = null
        var pointName: String? = null
        var pointContent: String? = null
        var pointPic: String? = null
    }
}
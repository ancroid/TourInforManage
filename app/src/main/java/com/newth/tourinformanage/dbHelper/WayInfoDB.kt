package com.newth.tourinformanage.dbHelper

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.newth.tourinformanage.MyApplication
import com.newth.tourinformanage.bean.WayInfo

/**
 * Created by Mr.chen on 2017/12/6.
 */

class WayInfoDB private constructor() {
    private var db: SQLiteDatabase

    init {
        val wayinfoDBhelper = WayInfoDBHelper(MyApplication.getAppContext(), dbName, null, version)
        db = wayinfoDBhelper.writableDatabase
    }

    companion object {
        val dbName: String = "WayInfo"
        val version: Int = 1
        fun get(): WayInfoDB = inner.db
    }

    private object inner {
        val db = WayInfoDB()
    }

    fun saveWayInfo(wayInfo: WayInfo?) {
        if (wayInfo != null) {
            val contentValues = ContentValues()
            contentValues.put("wayName", wayInfo.wayName)
            contentValues.put("wayLocation", wayInfo.wayLocation)
            contentValues.put("wayKind", wayInfo.wayKind)
            contentValues.put("wayContent", wayInfo.wayContent)
            val insertid = db.insert("WayInfo", null, contentValues)
            savePointInfo(insertid, wayInfo.wayPointList!!)
        }
    }

    private fun savePointInfo(id: Long, list: ArrayList<WayInfo.Point>) {
        for (i in list.indices) {
            val contentValues = ContentValues()
            val point = list[i]
            contentValues.put("wayTag", id)
            contentValues.put("pointName", point.pointName)
            contentValues.put("pointContent", point.pointContent)
            contentValues.put("pointUri", point.pointPic)
            db.insert("PointInfo", null, contentValues)
        }
    }

    fun getonlyWayInfo(): ArrayList<WayInfo> {
        val list = ArrayList<WayInfo>()
        val cursor = db.query("WayInfo", null, null, null, null, null, "id desc")
        if (cursor!!.moveToFirst()) {
            do {
                val way = WayInfo()
                way.id = cursor.getInt(cursor.getColumnIndex("id"))
                way.wayName = cursor.getString(cursor.getColumnIndex("wayName"))
                way.wayLocation = cursor.getString(cursor.getColumnIndex("wayLocation"))
                way.wayKind = cursor.getString(cursor.getColumnIndex("wayKind"))
                way.wayContent = cursor.getString(cursor.getColumnIndex("wayContent"))
                list.add(way)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
    fun getWayInfoByName(name:String): ArrayList<WayInfo>{
        val list = ArrayList<WayInfo>()
        val cursor = db.query("WayInfo", null, "wayName=?", arrayOf(name), null, null, "id desc")
        if (cursor!!.moveToFirst()) {
            do {
                val way = WayInfo()
                way.id = cursor.getInt(cursor.getColumnIndex("id"))
                way.wayName = cursor.getString(cursor.getColumnIndex("wayName"))
                way.wayLocation = cursor.getString(cursor.getColumnIndex("wayLocation"))
                way.wayKind = cursor.getString(cursor.getColumnIndex("wayKind"))
                way.wayContent = cursor.getString(cursor.getColumnIndex("wayContent"))
                list.add(way)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getWayAndPointByID(id: Int): ArrayList<WayInfo> {
        val list = ArrayList<WayInfo>()
        val cursor = db.query("WayInfo", null, "id=?", arrayOf(id.toString()), null, null, "id desc")
        if (cursor!!.moveToFirst()) {
            do {
                val way = WayInfo()
                way.id = cursor.getInt(cursor.getColumnIndex("id"))
                way.wayName = cursor.getString(cursor.getColumnIndex("wayName"))
                way.wayLocation = cursor.getString(cursor.getColumnIndex("wayLocation"))
                way.wayKind = cursor.getString(cursor.getColumnIndex("wayKind"))
                way.wayContent = cursor.getString(cursor.getColumnIndex("wayContent"))
                way.wayPointList = getPointByTag(way.id!!)
                list.add(way)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun isHavePoint(tag: Int): Boolean {
        val cursor = db.query("PointInfo", null, "wayTag=?", arrayOf(tag.toString()), null, null, "id desc")
        if (cursor!!.moveToFirst()) {
            do {
                if ((cursor.getString(cursor.getColumnIndex("pointName"))).isNotEmpty()) {
                    return true
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return false
    }

    private fun getPointByTag(tag: Int): ArrayList<WayInfo.Point> {
        val list = ArrayList<WayInfo.Point>()
        val cursor = db.query("PointInfo", null, "wayTag=?", arrayOf(tag.toString()), null, null, "id desc")
        if (cursor!!.moveToFirst()) {
            do {
                val point = WayInfo.Point()
                point.id = cursor.getInt(cursor.getColumnIndex("id"))
                point.pointName = cursor.getString(cursor.getColumnIndex("pointName"))
                point.pointPic = cursor.getString(cursor.getColumnIndex("pointUri"))
                point.pointContent = cursor.getString(cursor.getColumnIndex("pointContent"))
                list.add(point)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun deleteWayInfo(id: Int, tag: Boolean) {
        db.delete("WayInfo", "id=?", arrayOf(id.toString()))
        if (tag) {
            deletePointByTag(id)
        }
    }

    private fun deletePointByTag(tag: Int) {
        db.delete("PointInfo", "wayTag=?", arrayOf(tag.toString()))
    }

    fun editWayInfo(id: Int, wayInfo: WayInfo?) {
        if (wayInfo != null) {
            val contentValues = ContentValues()
            contentValues.put("wayName", wayInfo.wayName)
            contentValues.put("wayLocation", wayInfo.wayLocation)
            contentValues.put("wayKind", wayInfo.wayKind)
            contentValues.put("wayContent", wayInfo.wayContent)
            db.update("WayInfo", contentValues, "id=?", arrayOf(id.toString()))
            deletePointByTag(id)
            savePointInfo(id.toLong(), wayInfo.wayPointList!!)
        }
    }
}

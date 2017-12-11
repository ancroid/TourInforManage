package com.newth.tourinformanage.dbHelper

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.newth.tourinformanage.MyApplication
import com.newth.tourinformanage.bean.GuideInfo

/**
 * Created by Mr.chen on 2017/12/10.
 */

class TourGuideDB private constructor() {
    private var db: SQLiteDatabase

    companion object {
        val dbName: String = "tourGuide"
        val version: Int = 1
        fun get(): TourGuideDB = TourGuideDB.inner.db
    }

    init {
        val guideinfoDBhelper = TourGuideDBHelper(MyApplication.getAppContext(), dbName, null, version)
        db = guideinfoDBhelper.writableDatabase
    }

    private object inner {
        val db = TourGuideDB()
    }

    fun saveGuideInfo(guideInfo: GuideInfo?) {
        if (guideInfo != null) {
            val contentValues = ContentValues()
            contentValues.put("guideName", guideInfo.guideName)
            contentValues.put("guideAge", guideInfo.guideAge)
            contentValues.put("guideGender", guideInfo.guideGender)
            contentValues.put("guidePort", guideInfo.guidePort)
            contentValues.put("guideWorkTime", guideInfo.guideWorkTime)
            contentValues.put("guideTourTarget", guideInfo.guideTargetTour)
            contentValues.put("guidePhone", guideInfo.guidePhone)
            contentValues.put("guideContent", guideInfo.guideContent)
            db.insert("GuideInfo", null, contentValues)
        }
    }

    fun getGuideList(): ArrayList<GuideInfo> {
        val list = ArrayList<GuideInfo>()
        val cursor = db.query("GuideInfo", null, null, null, null, null, "id desc")
        if (cursor!!.moveToFirst()) {
            do {
                val guide = GuideInfo()
                guide.id = cursor.getInt(cursor.getColumnIndex("id"))
                guide.guideName = cursor.getString(cursor.getColumnIndex("guideName"))
                guide.guideAge = cursor.getString(cursor.getColumnIndex("guideAge"))
                guide.guideGender = cursor.getString(cursor.getColumnIndex("guideGender"))
                guide.guidePort = cursor.getString(cursor.getColumnIndex("guidePort"))
                guide.guideWorkTime = cursor.getString(cursor.getColumnIndex("guideWorkTime"))
                guide.guideTargetTour = cursor.getString(cursor.getColumnIndex("guideTourTarget"))
                guide.guidePhone = cursor.getString(cursor.getColumnIndex("guidePhone"))
                guide.guideContent = cursor.getString(cursor.getColumnIndex("guideContent"))
                list.add(guide)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getGuideByID(id: Int): GuideInfo {
        val list = ArrayList<GuideInfo>()
        val cursor = db.query("GuideInfo", null, "id=?", arrayOf(id.toString()), null, null, null)
        if (cursor!!.moveToFirst()) {
            do {
                val guide = GuideInfo()
                guide.id = cursor.getInt(cursor.getColumnIndex("id"))
                guide.guideName = cursor.getString(cursor.getColumnIndex("guideName"))
                guide.guideAge = cursor.getString(cursor.getColumnIndex("guideAge"))
                guide.guideGender = cursor.getString(cursor.getColumnIndex("guideGender"))
                guide.guidePort = cursor.getString(cursor.getColumnIndex("guidePort"))
                guide.guideWorkTime = cursor.getString(cursor.getColumnIndex("guideWorkTime"))
                guide.guideTargetTour = cursor.getString(cursor.getColumnIndex("guideTourTarget"))
                guide.guidePhone = cursor.getString(cursor.getColumnIndex("guidePhone"))
                guide.guideContent = cursor.getString(cursor.getColumnIndex("guideContent"))
                list.add(guide)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list[0]
    }

    fun deleteGuideByID(id: Int) {
        db.delete("GuideInfo", "id=?", arrayOf(id.toString()))
    }

    fun editSceneByID(id: Int, guideInfo: GuideInfo?) {
        if (guideInfo != null) {
            val contentValues = ContentValues()
            contentValues.put("guideName", guideInfo.guideName)
            contentValues.put("guideAge", guideInfo.guideAge)
            contentValues.put("guideGender", guideInfo.guideGender)
            contentValues.put("guidePort", guideInfo.guidePort)
            contentValues.put("guideWorkTime", guideInfo.guideWorkTime)
            contentValues.put("guideTourTarget", guideInfo.guideTargetTour)
            contentValues.put("guidePhone", guideInfo.guidePhone)
            contentValues.put("guideContent", guideInfo.guideContent)
            db.update("GuideInfo", contentValues, "id=?", arrayOf(id.toString()))
        }
    }

}

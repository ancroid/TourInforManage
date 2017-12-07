package com.newth.tourinformanage.dbHelper

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.newth.tourinformanage.MyApplication
import com.newth.tourinformanage.bean.SceneInfo
import com.newth.tourinformanage.bean.WayInfo

/**
 * Created by Mr.chen on 2017/12/3.
 */

class SceneInfoDB private constructor() {
    private var db: SQLiteDatabase

    companion object {
        val dbName: String = "scenery"
        val version: Int = 1
        fun get(): SceneInfoDB = SceneInfoDB.inner.db
    }

    init {
        val sceneinfoDBhelper = SceneInfoDBHelper(MyApplication.getAppContext(), dbName, null, version)
        db = sceneinfoDBhelper.writableDatabase
    }

    private object inner {
        val db = SceneInfoDB()
    }

    fun saveSceneInfo(sceneinfo: SceneInfo?) {
        if (sceneinfo != null) {
            val contentValues = ContentValues()
            contentValues.put("sceneName", sceneinfo.sceneName)
            contentValues.put("sceneLocation", sceneinfo.sceneLocation)
            contentValues.put("sceneKind", sceneinfo.sceneKind)
            contentValues.put("sceneContent", sceneinfo.sceneContent)
            contentValues.put("scenePicList", savePicList(sceneinfo.scenePicList!!))
            db.insert("SceneInfo", null, contentValues)
        }
    }


    fun getSceneInfo(): ArrayList<SceneInfo> {
        val list = ArrayList<SceneInfo>()
        val cursor = db.query("SceneInfo", null, null, null, null, null, null)
        if (cursor!!.moveToFirst()) {
            do {
                val scene = SceneInfo()
                scene.id = cursor.getInt(cursor.getColumnIndex("id"))
                scene.sceneName = cursor.getString(cursor.getColumnIndex("sceneName"))
                scene.sceneLocation = cursor.getString(cursor.getColumnIndex("sceneLocation"))
                scene.sceneKind = cursor.getString(cursor.getColumnIndex("sceneKind"))
                scene.sceneContent = cursor.getString(cursor.getColumnIndex("sceneContent"))
                scene.scenePicList = getPiclist(cursor.getString(cursor.getColumnIndex("scenePicList")))
                list.add(scene)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getSceneInfoByID(id: Int): ArrayList<SceneInfo> {
        val list = ArrayList<SceneInfo>()
        val cursor = db.query("SceneInfo", null, "id=?", arrayOf(id.toString()), null, null, null)
        if (cursor!!.moveToFirst()) {
            do {
                val scene = SceneInfo()
                scene.id = cursor.getInt(cursor.getColumnIndex("id"))
                scene.sceneName = cursor.getString(cursor.getColumnIndex("sceneName"))
                scene.sceneLocation = cursor.getString(cursor.getColumnIndex("sceneLocation"))
                scene.sceneKind = cursor.getString(cursor.getColumnIndex("sceneKind"))
                scene.sceneContent = cursor.getString(cursor.getColumnIndex("sceneContent"))
                scene.scenePicList = getPiclist(cursor.getString(cursor.getColumnIndex("scenePicList")))
                list.add(scene)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun deleteSceneByID(id: Int): Boolean {
        db.delete("SceneInfo", "id=?", arrayOf(id.toString()))
        return true
    }

    fun editSceneByID(id: Int, sceneinfo: SceneInfo?): Boolean {
        if (sceneinfo != null) {
            val contentValues = ContentValues()
            contentValues.put("sceneName", sceneinfo.sceneName)
            contentValues.put("sceneLocation", sceneinfo.sceneLocation)
            contentValues.put("sceneKind", sceneinfo.sceneKind)
            contentValues.put("sceneContent", sceneinfo.sceneContent)
            contentValues.put("scenePicList", savePicList(sceneinfo.scenePicList!!))
            db.update("SceneInfo", contentValues, "id=?", arrayOf(id.toString()))
        }
        return true
    }

    private fun savePicList(list: ArrayList<String>): String {
        var locationTemp = ""
        for (i in list.indices) {
            if (i == list.size - 1) {
                locationTemp += list[i]
            } else {
                locationTemp = locationTemp + list[i] + ","
            }
        }
        return locationTemp
    }

    private fun getPiclist(stringList: String): ArrayList<String> {
        val list = ArrayList<String>()
        val strs = stringList.split(",")
        strs.indices.mapTo(list) { strs[it] }
        return list
    }
}

package com.newth.tourinformanage.dbHelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Mr.chen on 2017/12/3.
 */
class SceneInfoDBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version){
    companion object {
        val CREATE_SCENEINFO="create table SceneInfo ("+
                "id integer primary key autoincrement,"+
                "sceneName text,"+
                "sceneLocation text,"+
                "sceneKind text,"+
                "sceneContent text,"+
                "scenePicList text)"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_SCENEINFO)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

}
package com.newth.tourinformanage.dbHelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Mr.chen on 2017/12/10.
 */

class TourGuideDBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        val CREATE_GUIDEINFO = "create table GuideInfo (" +
                "id integer primary key autoincrement," +
                "guideName text," +
                "guideAge text," +
                "guideGender text," +
                "guidePort text," +
                "guideWorkTime text," +
                "guideTourTarget text," +
                "guidePhone text," +
                "guideContent text)"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_GUIDEINFO)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {

    }
}

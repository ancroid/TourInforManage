package com.newth.tourinformanage.dbHelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Mr.chen on 2017/12/6.
 */

class WayInfoDBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_WAYINFO)
        p0?.execSQL(CREATE_POINTINFO)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    companion object {
        val CREATE_WAYINFO = "create table WayInfo (" +
                "id integer primary key autoincrement," +
                "wayName text," +
                "wayLocation text," +
                "wayKind text," +
                "wayContent text)"
        val CREATE_POINTINFO = "create table PointInfo (" +
                "id integer primary key autoincrement," +
                "wayTag integer,"+
                "pointName text," +
                "pointContent text,"+
                "pointUri text)"

    }
}

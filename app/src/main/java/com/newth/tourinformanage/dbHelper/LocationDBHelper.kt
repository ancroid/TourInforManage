package com.newth.tourinformanage.dbHelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Mr.chen on 2017/11/29.
 */

class LocationDBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    companion object {
        val CREATE_PROVINCE="create table Province ("+
                "id integer primary key autoincrement,"+
                "provinceName text,"+
                "provinceCode integer)"
        val CREATE_CITY="create table City ("+
                "id integer primary key autoincrement,"+
                "provinceID integer,"+
                "cityName text,"+
                "cityCode integer)"
        val CREATE_COUNTY="create table County ("+
                "id integer primary key autoincrement,"+
                "cityID integer,"+
                "countyName text)"

    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_PROVINCE)
        p0?.execSQL(CREATE_CITY)
        p0?.execSQL(CREATE_COUNTY)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}

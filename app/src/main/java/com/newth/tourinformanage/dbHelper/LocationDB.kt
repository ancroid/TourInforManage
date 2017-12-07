package com.newth.tourinformanage.dbHelper

import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import com.newth.tourinformanage.MyApplication
import com.newth.tourinformanage.bean.County
import com.newth.tourinformanage.bean.Province
import com.newth.tourinformanage.bean.City


/**
 * Created by Mr.chen on 2017/11/29.
 */

class LocationDB private constructor() {
    private var db: SQLiteDatabase

    init {
        val locationDBhelper = LocationDBHelper(MyApplication.getAppContext(), dbName, null, version)
        db = locationDBhelper.writableDatabase
    }

    companion object {
        val dbName: String = "location"
        val version: Int = 1
        fun get(): LocationDB = inner.db
    }

    private object inner {
        val db = LocationDB()
    }

    fun saveProvince(province: Province?) {
        if (province != null) {
            val contentValues = ContentValues()
            contentValues.put("provinceName", province.provinceName)
            contentValues.put("provinceCode", province.provinceCode)
            db.insert("Province", null, contentValues)
        }
    }

    fun saveCity(city: City?) {
        if (city != null) {
            val contentValues = ContentValues()
            contentValues.put("cityName", city.cityName)
            contentValues.put("cityCode", city.cityCode)
            contentValues.put("provinceID", city.provinceID)
            db.insert("City", null, contentValues)
        }
    }

    fun saveCounty(county: County?) {
        if (county != null) {
            val contentValues = ContentValues()
            contentValues.put("countyName", county.countyName)
            contentValues.put("cityID", county.cityID)
            db.insert("County", null, contentValues)
        }
    }

    fun getProvinceFromDB(): ArrayList<Province> {
        val list = ArrayList<Province>()
        val cursor = db.query("Province", null, null, null, null, null, null)
        if (cursor!!.moveToFirst()) {
            do {
                val province = Province()
                province.provinceName = cursor.getString(cursor.getColumnIndex("provinceName"))
                province.provinceCode = cursor.getInt(cursor.getColumnIndex("provinceCode"))
                list.add(province)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getCityFromDB(provinceID: Int): ArrayList<City> {
        val list = ArrayList<City>()
        val cursor = db.query("City", null, "provinceID=?", arrayOf(provinceID.toString()), null, null, null)
        if (cursor!!.moveToFirst()) {
            do {
                val city = City()
                city.cityName = cursor.getString(cursor.getColumnIndex("cityName"))
                city.cityCode = cursor.getInt(cursor.getColumnIndex("cityCode"))
                list.add(city)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getCountyFromDB(cityID: Int): ArrayList<County> {
        val list = ArrayList<County>()
        val cursor = db.query("County", null, "cityID=?", arrayOf(cityID.toString()), null, null, null)
        if (cursor!!.moveToFirst()) {
            do {
                val county = County()
                county.countyName = cursor.getString(cursor.getColumnIndex("countyName"))
                list.add(county)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
}

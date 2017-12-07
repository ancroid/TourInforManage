package com.newth.tourinformanage.util

import com.newth.tourinformanage.bean.City
import com.newth.tourinformanage.bean.County
import com.newth.tourinformanage.bean.Province
import com.newth.tourinformanage.dbHelper.LocationDB
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

/**
 * Created by Mr.chen on 2017/11/28.
 */
class HttpUtil {
    companion object {
        fun sendOkhttpRequest(address: String, callback: okhttp3.Callback) {
            val client = OkHttpClient()
            val request = Request.Builder().url(address).build()
            client.newCall(request).enqueue(callback)
        }

        fun handleProvinceResponse(response: String): Boolean {
            if (response.isNotEmpty()) {
                val array = JSONArray(response)
                var i = 0
                while (i < array.length()) {
                    val jsonobject = array.getJSONObject(i)
                    val province = Province()
                    province.provinceName = jsonobject.getString("name")
                    province.provinceCode = jsonobject.getInt("id")
                    val db=LocationDB.get()
                    db.saveProvince(province)
                    i++
                }
                return true
            }
            return false
        }

        fun hanleCityResponse(response: String,provinceID:Int): Boolean {
            if (response.isNotEmpty()) {
                val array = JSONArray(response)
                var i = 0
                while (i < array.length()) {
                    val jsobject = array.getJSONObject(i)
                    val city = City()
                    city.cityName = jsobject.getString("name")
                    city.cityCode = jsobject.getInt("id")
                    city.provinceID=provinceID
                    val db=LocationDB.get()
                    db.saveCity(city)
                    i++
                }
                return true
            }
            return false
        }

        fun hanleCountyResponse(response: String,cityID:Int): Boolean {
            if (response.isNotEmpty()) {
                val array = JSONArray(response)
                var i = 0
                while (i < array.length()) {
                    val jsobject = array.getJSONObject(i)
                    val county = County()
                    county.countyName = jsobject.getString("name")
                    county.cityID=cityID
                    val db=LocationDB.get()
                    db.saveCounty(county)
                    i++
                }
                return true
            }
            return false
        }

    }
}
package com.newth.tourinformanage.util

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.newth.tourinformanage.MyApplication
import com.newth.tourinformanage.R
import com.newth.tourinformanage.adapter.LocationAdapter
import com.newth.tourinformanage.bean.City
import com.newth.tourinformanage.bean.County
import com.newth.tourinformanage.bean.IPickerViewData
import com.newth.tourinformanage.bean.Province
import com.newth.tourinformanage.dbHelper.LocationDB
import okhttp3.Call
import okhttp3.Response
import java.io.IOException

/**
 * Created by Mr.chen on 2017/12/1.
 */

class PickLocationActivity : AppCompatActivity() {
    private lateinit var provinceList: ArrayList<Province>
    private lateinit var cityList: ArrayList<City>
    private lateinit var countyList: ArrayList<County>

    private var dataList = ArrayList<IPickerViewData>()

    private var selectProvince: String = ""
    private var selectCity: String = ""
    private var selectCounty: String = ""

    private var selectProvinceID = 0
    private var selectCityID = 0
    private var selectCountyID = 0

    private lateinit var toolbar: Toolbar
    private lateinit var text_tool_title: TextView
    private lateinit var reclerview: RecyclerView
    private var currentLevel = 0

    private lateinit var myadapter: LocationAdapter
    private var db: LocationDB = LocationDB.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_choose_location)
        initView()
        queryProvince()
    }


    private fun initView() {
        toolbar = findViewById(R.id.tool_bar)
        text_tool_title = findViewById(R.id.toolbar_title)
        reclerview = findViewById(R.id.recycler_location)
        initToolBar()
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        var actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.title = ""
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        text_tool_title.text = "中国"
    }

    private fun initRecycler() {
        myadapter = LocationAdapter(dataList)
        val linerManager = LinearLayoutManager(this)
        reclerview.layoutManager = linerManager
        reclerview.adapter = myadapter
        myadapter.setOnItemClickListener { adapter, view, position ->
            currentLevel++
            when (currentLevel) {
                1 -> {
                    selectProvince = dataList[position].getPickerViewText()!! + "（省）"
                    text_tool_title.text = selectProvince
                    selectProvinceID = dataList[position].getTargetID()!!
                    queryCity(selectProvinceID)
                }
                2 -> {
                    selectCity = dataList[position].getPickerViewText()!! + "（市）"
                    text_tool_title.text = selectCity
                    selectCityID = dataList[position].getTargetID()!!
                    queryCounty(selectProvinceID, selectCityID)
                }
                3 -> {
                    selectCounty = dataList[position].getPickerViewText()!! + "（县/区）"
                    sendResult()
                }
            }
        }
    }

    private fun setData() {
        myadapter.setNewData(dataList)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_scene_info, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                currentLevel--
                if (currentLevel < 0) {
                    finish()
                }
                when (currentLevel) {
                    1 -> {
                        queryCity(selectProvinceID)
                        text_tool_title.text = selectProvince
                    }
                    0 -> {
                        text_tool_title.text = "中国"
                        queryProvince()
                    }
                }
            }
            R.id.edit_scene_info -> {
                sendResult()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendResult() {
        val intent = Intent()
        intent.putExtra("location", selectProvince + selectCity + selectCounty)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun queryProvince() {
        provinceList = db.getProvinceFromDB()
        if (provinceList.isNotEmpty()) {
            dataList.clear()
            for (i in provinceList.indices) {
                provinceList[i].let { dataList.add(it) }
            }
            initRecycler()
        } else {
            getProvinceFromServer("http://guolin.tech/api/china")
        }
    }

    private fun queryCity(provinceID: Int) {
        cityList = db.getCityFromDB(provinceID)
        if (cityList.isNotEmpty()) {
            dataList.clear()
            for (i in cityList.indices) {
                Log.d("mmmm", cityList[i].cityName)
                cityList[i].let { dataList.add(it) }
            }
            setData()
        } else {
            getCityFromServer("http://guolin.tech/api/china/" + provinceID, provinceID)
        }
    }

    private fun queryCounty(provinceID: Int, cityID: Int) {
        countyList = db.getCountyFromDB(cityID)
        if (countyList.isNotEmpty()) {
            dataList.clear()
            for (i in countyList.indices) {
                countyList[i].let { dataList.add(it) }
            }
            setData()
        } else {
            getCountyFromServer("http://guolin.tech/api/china/" + provinceID + "/" + cityID, provinceID, cityID)
        }
    }

    private fun getProvinceFromServer(address: String) {
        HttpUtil.sendOkhttpRequest(address, object : okhttp3.Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                runOnUiThread {
                    Toast.makeText(MyApplication.getAppContext(), "获取失败", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call?, response: Response?) {
                var result = false
                if (response != null) {
                    result = HttpUtil.handleProvinceResponse(response.body()!!.string())
                }
                if (result) {
                    runOnUiThread {
                        queryProvince()
                    }
                }
            }

        })
    }

    private fun getCityFromServer(address: String, provinceID: Int) {
        HttpUtil.sendOkhttpRequest(address, object : okhttp3.Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                runOnUiThread {
                    Toast.makeText(MyApplication.getAppContext(), "获取失败", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call?, response: Response?) {
                var result = false
                if (response != null) {
                    result = HttpUtil.hanleCityResponse(response.body()!!.string(), provinceID)
                }
                if (result) {
                    runOnUiThread {
                        queryCity(provinceID)
                    }
                }
            }
        })
    }

    private fun getCountyFromServer(address: String, provinceID: Int, cityID: Int) {
        HttpUtil.sendOkhttpRequest(address, object : okhttp3.Callback {
            override fun onResponse(call: Call?, response: Response?) {
                var result = false
                if (response != null) {
                    result = HttpUtil.hanleCountyResponse(response.body()!!.string(), cityID)
                }
                if (result) {
                    runOnUiThread {
                        queryCounty(provinceID, cityID)
                    }
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                runOnUiThread {
                    Toast.makeText(MyApplication.getAppContext(), "获取失败", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}

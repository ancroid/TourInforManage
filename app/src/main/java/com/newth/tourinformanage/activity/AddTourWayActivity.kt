package com.newth.tourinformanage.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.goyourfly.multi_picture.ImageLoader
import com.goyourfly.multi_picture.MultiPictureView
import com.newth.tourinformanage.R
import com.newth.tourinformanage.adapter.TourWayAdapter
import com.newth.tourinformanage.bean.WayInfo
import com.newth.tourinformanage.dbHelper.WayInfoDB
import com.newth.tourinformanage.util.MatisseUtil
import com.squareup.picasso.Picasso
import com.zhihu.matisse.Matisse
import java.util.ArrayList

/**
 * Created by Mr.chen on 2017/12/4.
 */

class AddTourWayActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var text_tool_title: TextView
    private lateinit var recycler: RecyclerView
    private lateinit var myAdapter: TourWayAdapter

    private var tag = 0
    private var id = 0
    private var dataList = ArrayList<WayInfo>()
    private var db: WayInfoDB = WayInfoDB.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_way)
        toolbar = findViewById(R.id.tool_bar)
        text_tool_title = findViewById(R.id.toolbar_title)
        recycler = findViewById(R.id.recycler_add_way)

        initView()

        val intent = intent
        tag = intent.getIntExtra("tag", 0)
        id = intent.getIntExtra("id", 0)
        if (tag == 2) {
            //修改景点信息
            queryDB(id)
        }else{
            initRecycler()
        }
    }

    private fun initView() {
        MultiPictureView.setImageLoader(object : ImageLoader {
            override fun loadImage(image: ImageView, uri: Uri) {
                Picasso.with(this@AddTourWayActivity)
                        .load(uri)
                        .fit()
                        .into(image)
            }
        })
        initToolBar()
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        var actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.title = ""
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        text_tool_title.text = "添加旅游线路信息"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_scene_info, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
            R.id.edit_scene_info -> {
                if (myAdapter.collecInfo()) {
                    val wayinfo = myAdapter.getWayInfo()
                    db.saveWayInfo(wayinfo)
                    Log.d("addt", wayinfo.wayName + "  " + wayinfo.wayPointList!!.size)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecycler() {
        val list = arrayListOf(0)
        myAdapter = TourWayAdapter(list, this)
        val linerManager = LinearLayoutManager(this)
        recycler.layoutManager = linerManager
        recycler.adapter = myAdapter
        myAdapter.addFootView(0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                MatisseUtil.REQUEST_CODE_CHOOSE -> {
                    val uriList = Matisse.obtainResult(data)
                    myAdapter.setPic(uriList[0])
                }
            //getlocation
                0 -> {
                    if (data != null) {
                        myAdapter.setLocation(data.getStringExtra("location"))
                    }
                }
            }
        }
    }

    private fun queryDB(id: Int) {
        dataList = db.getWayAndPointByID(id)
        if (dataList.isNotEmpty()) {
            initRecycler()
        }
    }
    fun showInfo() {
        if (tag==2){
            val wayInfo = dataList[0]
            myAdapter.setInfoByEdit(wayInfo)
        }
    }
}

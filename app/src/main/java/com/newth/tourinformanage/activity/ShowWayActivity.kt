package com.newth.tourinformanage.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.goyourfly.multi_picture.ImageLoader
import com.goyourfly.multi_picture.MultiPictureView

import com.newth.tourinformanage.R
import com.newth.tourinformanage.adapter.ShowWayAdapter
import com.newth.tourinformanage.bean.WayInfo
import com.newth.tourinformanage.bean.WayMutipleItem
import com.newth.tourinformanage.dbHelper.WayInfoDB
import com.squareup.picasso.Picasso

/**
 * Created by Mr.chen on 2017/12/7.
 */

class ShowWayActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var text_tool_title: TextView
    private lateinit var reclerview: RecyclerView
    private var db: WayInfoDB = WayInfoDB.get()
    private var dataList = ArrayList<WayInfo>()
    private lateinit var myAdapter: ShowWayAdapter

    private var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_way)
        initView()
    }

    private fun initView() {
        toolbar = findViewById(R.id.tool_bar)
        text_tool_title = findViewById(R.id.toolbar_title)
        reclerview = findViewById(R.id.recycler_wayinfo)
        initMultiputure()
        initToolBar()
        val intent = intent
        id = intent.getIntExtra("id", 0)
        queryWayInfo()
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        var actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.title = ""
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        text_tool_title.text = "旅游线路详细信息"
    }

    private fun queryWayInfo() {
        dataList = db.getWayAndPointByID(id)
        if (dataList.isNotEmpty()) {
            initRecycler()
        }
    }

    private fun initRecycler() {
        val list = ArrayList<WayMutipleItem>()
        val item = WayMutipleItem(1, dataList[0])
        list.add(item)
        for (i in dataList[0].wayPointList!!.indices) {
            val item = WayMutipleItem(2, dataList[0].wayPointList!![i])
            list.add(item)
        }
        myAdapter = ShowWayAdapter(list)
        val linerManager = LinearLayoutManager(this)
        reclerview.layoutManager = linerManager
        reclerview.adapter = myAdapter
    }

    private fun initMultiputure() {
        MultiPictureView.setImageLoader(object : ImageLoader {
            override fun loadImage(image: ImageView, uri: Uri) {
                println("TAG:" + uri)
                Picasso.with(this@ShowWayActivity)
                        .load(uri)
                        .fit()
                        .error(R.drawable.ic_defeat)
                        .into(image)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_scene_info, menu)
        menu!!.findItem(R.id.edit_scene_info).title = "编辑"
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
            R.id.edit_scene_info -> {
                val intent = Intent(this, AddTourWayActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("tag", 2)
                startActivityForResult(intent, 1)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    if (data != null) {
                        id = data.getIntExtra("id", 0)
                        queryWayInfo()
                    }

                }
            }
        }
    }
}

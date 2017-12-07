package com.newth.tourinformanage.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.goyourfly.multi_picture.ImageLoader
import com.goyourfly.multi_picture.MultiPictureView
import com.newth.tourinformanage.R
import com.newth.tourinformanage.bean.SceneInfo
import com.newth.tourinformanage.dbHelper.SceneInfoDB
import com.squareup.picasso.Picasso

/**
 * Created by Mr.chen on 2017/12/4.
 */

class ShowSceneActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var text_location: TextView
    private lateinit var text_scen_name: TextView
    private lateinit var text_scen_content: TextView
    private lateinit var multi_pic: MultiPictureView
    private lateinit var text_tool_title: TextView
    private var db: SceneInfoDB = SceneInfoDB.get()
    private var dataList = ArrayList<SceneInfo>()

    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_scenery)
        initView()
    }

    private fun initView() {
        toolbar = findViewById(R.id.tool_bar)
        text_location = findViewById(R.id.text_location_show)
        text_scen_content = findViewById(R.id.text_scene_content)
        text_scen_name = findViewById(R.id.text_scene_name)
        multi_pic = findViewById(R.id.multiple_scenery)
        text_tool_title = findViewById(R.id.toolbar_title)
        initToolBar()
        initMultiputure()
        val intent = intent
        id = intent.getIntExtra("id", 0)
        queryDB(id)
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        var actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.title = ""
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        text_tool_title.text = "景点详细信息"
    }

    private fun initMultiputure() {
        MultiPictureView.setImageLoader(object : ImageLoader {
            override fun loadImage(image: ImageView, uri: Uri) {
                println("TAG:" + uri)
                Picasso.with(this@ShowSceneActivity)
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
                val intent = Intent(this, AddScenInfoActivity::class.java)
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
                        queryDB(id)
                    }
                }
            }
        }
    }

    private fun queryDB(id: Int) {
        dataList = db.getSceneInfoByID(id)
        if (dataList.isNotEmpty()) {
            showInfo()
        }
    }

    private fun showInfo() {
        val scene = dataList[0]
        text_scen_name.text = scene.sceneName
        text_scen_content.text = scene.sceneContent
        text_location.text = scene.sceneLocation
        val list = scene.scenePicList
        if (list != null && list.size > 0) {
            Log.d("mmm", list.size.toString())
            if (multi_pic.getList().size>0){
                multi_pic.getList().clear()
            }
            for (i in list.indices) {
                multi_pic.addItem(Uri.parse(list[i]))
            }
        }
    }

}

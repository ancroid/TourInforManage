package com.newth.tourinformanage.activity

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.goyourfly.multi_picture.ImageLoader
import com.goyourfly.multi_picture.MultiPictureView
import com.newth.tourinformanage.R
import com.newth.tourinformanage.bean.SceneInfo
import com.newth.tourinformanage.dbHelper.SceneInfoDB
import com.newth.tourinformanage.util.MatisseUtil
import com.newth.tourinformanage.util.PickLocationActivity
import com.sevenheaven.segmentcontrol.SegmentControl
import com.squareup.picasso.Picasso

import com.zhihu.matisse.Matisse
import java.lang.reflect.Array
import java.util.ArrayList


/**
 * Created by Mr.chen on 2017/11/26.
 */
class AddScenInfoActivity : AppCompatActivity() {
    private lateinit var edit_scen_name: EditText
    private lateinit var edit_scen_content: EditText
    private lateinit var text_location: TextView
    private lateinit var text_get_location: TextView
    private lateinit var segment_control: SegmentControl
    private lateinit var multi_pic: MultiPictureView
    private lateinit var toolbar: Toolbar
    private lateinit var text_tool_title: TextView
    private var uriList: List<Uri>? = null
    private var kindsArray = arrayOf("自然景观", "人文景观", "现代游乐", "其他")
    private var kindtag = "自然景观"

    private var tag = 0
    private var id = 0
    private var dataList = ArrayList<SceneInfo>()

    private var db: SceneInfoDB = SceneInfoDB.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_scen)
        initview()
        val intent = intent
        tag = intent.getIntExtra("tag", 0)
        id = intent.getIntExtra("id", 0)
        if (tag == 2) {
            //修改景点信息
            queryDB(id)
        }
    }

    private fun initview() {
        edit_scen_name = findViewById(R.id.edit_text_scene_name)
        edit_scen_content = findViewById(R.id.edit_scene_content)
        text_location = findViewById(R.id.text_location)
        text_get_location = findViewById(R.id.text_get_location)
        segment_control = findViewById(R.id.segment_control)
        multi_pic = findViewById(R.id.multiple_image)
        toolbar = findViewById(R.id.tool_bar)
        text_tool_title = findViewById(R.id.toolbar_title)
        initToolBar()
        initMutiplePicView()

        text_get_location.setOnClickListener {
            val intent = Intent(this, PickLocationActivity::class.java)
            startActivityForResult(intent, 0)
        }
        segment_control.setOnSegmentControlClickListener { index -> kindtag = kindsArray[index] }
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        var actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.title = ""
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        text_tool_title.text = "添加景点信息"
    }

    private fun initMutiplePicView() {
        MultiPictureView.setImageLoader(object : ImageLoader {
            override fun loadImage(image: ImageView, uri: Uri) {
                Picasso.with(this@AddScenInfoActivity)
                        .load(uri)
                        .fit()
                        .into(image)
            }
        })
        multi_pic.addClickCallback = object : MultiPictureView.AddClickCallback {
            override fun onAddClick(view: View) {
                val m = MatisseUtil(this@AddScenInfoActivity)
                m.start()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                MatisseUtil.REQUEST_CODE_CHOOSE -> {
                    uriList = Matisse.obtainResult(data)
                    if (multi_pic.getList().size > 0) {
                        multi_pic.getList().clear()
                    }
                    multi_pic.addItem(uriList!!)
                    Toast.makeText(this, "" + uriList!!.size, Toast.LENGTH_SHORT).show()
                }
            //getlocation
                0 -> {
                    if (data != null) {
                        text_location.text = data.getStringExtra("location")
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_scene_info, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
            R.id.edit_scene_info -> {
                if (collectInfo()) {
                    if (tag == 2) {
                        editInfoByID(id)
                    } else {
                        saveSceneinfo()
                    }
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun collectInfo(): Boolean {
        return !(getSceneName() == null || getLocation() == null || getSceneContent() == null)
    }

    private fun editInfoByID(id: Int) {
        val scene = SceneInfo()
        scene.sceneName = getSceneName()
        scene.sceneKind = getSceneKind()
        scene.sceneContent = getSceneContent()
        scene.sceneLocation = getLocation()
        scene.scenePicList = getImgList()
        if (db.editSceneByID(id, scene)) {
            val intent = Intent()
            intent.putExtra("id", id)
            setResult(Activity.RESULT_OK, intent)
        }
    }

    private fun saveSceneinfo() {
        val scene = SceneInfo()
        scene.sceneName = getSceneName()
        scene.sceneKind = getSceneKind()
        scene.sceneContent = getSceneContent()
        scene.sceneLocation = getLocation()
        scene.scenePicList = getImgList()
        db.saveSceneInfo(scene)
    }

    private fun getSceneName(): String? {
        return if (edit_scen_name.text.toString() == "") {
            Toast.makeText(this, "景点名称不能为空！", Toast.LENGTH_SHORT).show()
            null
        } else {
            edit_scen_name.text.toString()
        }
    }

    private fun getLocation(): String? {
        return if (text_location.text.toString() == "") {
            Toast.makeText(this, "景点位置不能为空！", Toast.LENGTH_SHORT).show()
            null
        } else {
            text_location.text.toString()
        }
    }

    private fun getSceneKind(): String = kindtag

    private fun getSceneContent(): String? {
        return if (edit_scen_content.text.toString() == "") {
            Toast.makeText(this, "景点详细描述不能为空！", Toast.LENGTH_SHORT).show()
            null
        } else {
            edit_scen_content.text.toString()
        }
    }

    private fun getImgList(): ArrayList<String> {
        val pathList = ArrayList<String>()
        for (i in multi_pic.getList().indices) {
            pathList.add(multi_pic.getList()[i].toString())
        }
        return pathList
    }

    private fun queryDB(id: Int) {
        dataList = db.getSceneInfoByID(id)
        if (dataList.isNotEmpty()) {
            showInfo()
        }
    }

    private fun showInfo() {
        val scene = dataList[0]
        edit_scen_name.setText(scene.sceneName)
        edit_scen_content.setText(scene.sceneContent)
        text_location.text = scene.sceneLocation
        kindtag = scene.sceneKind!!
        for (i in kindsArray.indices) {
            if (kindsArray[i] == kindtag) {
                segment_control.setSelectedIndex(i)
                break
            }
        }
        val list = scene.scenePicList
        if (list != null && list.size > 0) {
            if (multi_pic.getList().size > 0) {
                multi_pic.getList().clear()
            }
            for (i in list.indices) {
                multi_pic.addItem(Uri.parse(list[i]))
            }
        }
    }
}
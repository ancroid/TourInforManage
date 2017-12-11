package com.newth.tourinformanage.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

import com.newth.tourinformanage.R
import com.newth.tourinformanage.bean.GuideInfo
import com.newth.tourinformanage.dbHelper.TourGuideDB
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Mr.chen on 2017/12/11.
 */

class ShowGuideActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var text_tool_title: TextView
    private lateinit var img_port:CircleImageView
    private lateinit var text_name:TextView
    private lateinit var text_age:TextView
    private lateinit var text_gender:TextView
    private lateinit var text_work_time:TextView
    private lateinit var text_target_tour:TextView
    private lateinit var text_content:TextView
    private lateinit var text_phone:TextView

    private var db: TourGuideDB = TourGuideDB.get()
    private var dataList = ArrayList<GuideInfo>()

    private var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_guide)
        initView()
        val intent = intent
        id = intent.getIntExtra("id", 0)
        queryDB(id)
    }
    private fun initView() {
        toolbar = findViewById(R.id.tool_bar)
        text_tool_title = findViewById(R.id.toolbar_title)
        img_port=findViewById(R.id.img_guide)
        text_name=findViewById(R.id.text_guide_name)
        text_age=findViewById(R.id.text_guide_age)
        text_gender=findViewById(R.id.text_guide_gender)
        text_work_time=findViewById(R.id.text_guide_work_time)
        text_phone=findViewById(R.id.text_guide_phone)
        text_content=findViewById(R.id.text_guide_content)
        text_target_tour=findViewById(R.id.text_guide_target_tour)
        initToolBar()
    }
    private fun initToolBar() {
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.title = ""
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        text_tool_title.text = "导游详细信息"
    }

    private fun queryDB(id: Int) {
        if (dataList.size>0){
            dataList.clear()
        }
        dataList.add(db.getGuideByID(id))
        if (dataList.isNotEmpty()) {
            showInfo()
        }
    }
    private fun showInfo(){
        val guide=dataList[0]
        Picasso.with(this)
                .load(Uri.parse(guide.guidePort))
                .error(R.drawable.ic_defeat)
                .fit()
                .into(img_port)
        text_name.text=guide.guideName
        text_age.text=guide.guideAge
        text_gender.text=guide.guideGender
        text_target_tour.text=guide.guideTargetTour
        text_phone.text=guide.guidePhone
        text_content.text=guide.guideContent
        text_work_time.text=guide.guideWorkTime
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
                val intent = Intent(this, AddTourGuideActivity::class.java)
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
}

package com.newth.tourinformanage.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.newth.tourinformanage.R
import com.newth.tourinformanage.bean.GuideInfo
import com.newth.tourinformanage.dbHelper.TourGuideDB
import com.newth.tourinformanage.util.MatisseUtil
import com.sevenheaven.segmentcontrol.SegmentControl
import com.squareup.picasso.Picasso
import com.zhihu.matisse.Matisse
import java.util.ArrayList

/**
 * Created by Mr.chen on 2017/12/10.
 */

class AddTourGuideActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var text_tool_title: TextView
    private lateinit var img_port: ImageView
    private lateinit var edit_guide_name: EditText
    private lateinit var segement_gender: SegmentControl
    private lateinit var edit_guide_age: EditText
    private lateinit var edit_guide_work_time: EditText
    private lateinit var edit_guide_phone: EditText
    private lateinit var edit_guide_tour: EditText
    private lateinit var edit_guide_content: EditText

    private var imgUri = ""
    private var genderArray = arrayOf("男", "女")
    private var gendertag = "男"

    private var tag = 0
    private var id = 0
    private var dataList = ArrayList<GuideInfo>()

    private var db: TourGuideDB = TourGuideDB.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_guide)
        initView()
        val intent = intent
        tag = intent.getIntExtra("tag", 0)
        id = intent.getIntExtra("id", 0)
        if (tag == 2) {
            //修改导游信息
            queryDB(id)
        }
    }

    private fun initView() {
        toolbar = findViewById(R.id.tool_bar)
        text_tool_title = findViewById(R.id.toolbar_title)
        img_port = findViewById(R.id.img_guide)
        edit_guide_name = findViewById(R.id.edit_text_guide_name)
        edit_guide_age = findViewById(R.id.edit_text_guide_age)
        segement_gender = findViewById(R.id.segment_guide_gender)
        edit_guide_work_time = findViewById(R.id.edit_text_guide_work_time)
        edit_guide_phone = findViewById(R.id.edit_text_guide_phone)
        edit_guide_tour = findViewById(R.id.edit_text_guide_target_tour)
        edit_guide_content = findViewById(R.id.edit_text_guide_content)
        initToolBar()
        img_port.setOnClickListener {
            val m = MatisseUtil(this@AddTourGuideActivity)
            m.startPoint()
        }
        segement_gender.setOnSegmentControlClickListener { index -> gendertag = genderArray[index] }
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        var actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.title = ""
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        text_tool_title.text = "添加导游信息"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                MatisseUtil.REQUEST_CODE_CHOOSE -> {
                    val uriList = Matisse.obtainResult(data)
                    imgUri = uriList[0].toString()
                    Picasso.with(this)
                            .load(uriList[0])
                            .error(R.drawable.ic_defeat)
                            .fit()
                            .into(img_port)
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
                    if (tag==2){
                        editInfoByID()
                    }else{
                        saveInfo()
                    }
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
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
    private fun editInfoByID(){
        val guide = GuideInfo()
        guide.guidePort = imgUri
        guide.guideName = getName()
        guide.guideAge = getAge()
        guide.guideGender = getGender()
        guide.guidePhone = getPhoneNum()
        guide.guideWorkTime = getWorkTime()
        guide.guideTargetTour = getTourTarget()
        guide.guideContent = getGuideContent()
        db.editSceneByID(id,guide)
        val intent = Intent()
        intent.putExtra("id", id)
        setResult(Activity.RESULT_OK, intent)
    }
    private fun showInfo(){
        val guide=dataList[0]
        imgUri= guide.guidePort!!
        Picasso.with(this)
                .load(Uri.parse(imgUri))
                .error(R.drawable.ic_defeat)
                .fit()
                .into(img_port)
        edit_guide_name.setText(guide.guideName)
        edit_guide_age.setText(guide.guideAge)
        edit_guide_tour.setText(guide.guideTargetTour)
        edit_guide_phone.setText(guide.guidePhone)
        edit_guide_content.setText(guide.guideContent)
        edit_guide_work_time.setText(guide.guideWorkTime)
        gendertag=guide.guideGender!!
        for (i in genderArray.indices) {
            if (genderArray[i] == gendertag) {
                segement_gender.setSelectedIndex(i)
                break
            }
        }
    }
    private fun collectInfo(): Boolean {
        return !(getName() == null || getAge() == null || getPhoneNum() == null || getTourTarget() == null)
    }

    private fun saveInfo() {
        val guide = GuideInfo()
        guide.guidePort = imgUri
        guide.guideName = getName()
        guide.guideAge = getAge()
        guide.guideGender = getGender()
        guide.guidePhone = getPhoneNum()
        guide.guideWorkTime = getWorkTime()
        guide.guideTargetTour = getTourTarget()
        guide.guideContent = getGuideContent()
        db.saveGuideInfo(guide)
    }

    private fun getName(): String? {
        return if (edit_guide_name.text.toString() == "") {
            Toast.makeText(this, "导游姓名不能为空！", Toast.LENGTH_SHORT).show()
            null
        } else {
            edit_guide_name.text.toString()
        }
    }

    private fun getAge(): String? {
        return if (edit_guide_age.text.toString() == "") {
            Toast.makeText(this, "导游年龄不能为空！", Toast.LENGTH_SHORT).show()
            null
        } else {
            edit_guide_age.text.toString()
        }
    }

    private fun getGender(): String? {
        return gendertag
    }

    private fun getWorkTime(): String? {
        return edit_guide_work_time.text.toString()
    }

    private fun getPhoneNum(): String? {
        return if (edit_guide_phone.text.toString() == "") {
            Toast.makeText(this, "联系方式不能为空！", Toast.LENGTH_SHORT).show()
            null
        } else {
            edit_guide_phone.text.toString()
        }
    }

    private fun getTourTarget(): String? {
        return if (edit_guide_tour.text.toString() == "") {
            Toast.makeText(this, "负责景区不能为空！", Toast.LENGTH_SHORT).show()
            null
        } else {
            edit_guide_tour.text.toString()
        }
    }

    private fun getGuideContent(): String? {
        return edit_guide_content.text.toString()
    }

}

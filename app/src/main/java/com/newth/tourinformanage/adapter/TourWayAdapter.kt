package com.newth.tourinformanage.adapter


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goyourfly.multi_picture.ImageLoader
import com.goyourfly.multi_picture.MultiPictureView
import com.newth.tourinformanage.R
import com.newth.tourinformanage.activity.AddTourWayActivity
import com.newth.tourinformanage.bean.WayInfo
import com.newth.tourinformanage.util.MatisseUtil
import com.newth.tourinformanage.util.PickLocationActivity
import com.sevenheaven.segmentcontrol.SegmentControl
import com.squareup.picasso.Picasso

/**
 * Created by Mr.chen on 2017/12/4.
 */
class TourWayAdapter(data: List<Int>?, private var activity: Activity) : BaseQuickAdapter<Int, BaseViewHolder>(R.layout.item_way_one, data) {

    private lateinit var edit_name: EditText
    private lateinit var text_get_location: TextView
    private lateinit var text_show_location: TextView
    private lateinit var segment: SegmentControl
    private lateinit var edit_content: EditText
    private var kindsArray = arrayOf("观光旅游", "度假旅游", "专项旅游", "其他")
    private var kindtag = "观光旅游"

    private var wayinfo = WayInfo()
    private var location = ""
    private var viewholderList = ArrayList<myViewHolder>()
    private var tag_pic_postion = 0

    override fun convert(helper: BaseViewHolder, item: Int) {
        edit_name = helper.getView(R.id.edit_text_way_name)
        text_get_location = helper.getView(R.id.text_get_location)
        text_show_location = helper.getView(R.id.text_location)
        segment = helper.getView(R.id.segment_control)
        edit_content = helper.getView(R.id.edit_scene_content)

        text_get_location.setOnClickListener {
            getLocation()
        }
        segment.setOnSegmentControlClickListener { index -> kindtag = kindsArray[index] }

        (activity as AddTourWayActivity).showInfo()
    }

    private fun initClicklistener() {
        if (footerLayout.childCount == 1) {
            val view = footerLayout.getChildAt(footerLayout.childCount - 1)
            val btu_add: ImageButton = view.findViewById(R.id.add_foot)
            val btu_delete: ImageButton = view.findViewById(R.id.image_btu_delete)
            btu_add.setOnClickListener {
                addFootView(1)
            }
            btu_delete.setOnClickListener {
                if (footerLayout.childCount - 1 > 0)
                    footerLayout.removeViewAt(footerLayout.childCount - 2)
                if (viewholderList.size - 1 > 0) {
                    viewholderList.removeAt(viewholderList.size - 1)
                }
            }
        }
    }

    fun addFootView(tag: Int) {
        if (tag == 0) {
            val view = activity.layoutInflater.inflate(R.layout.item_add_more, null)
            addFooterView(view)
        } else {
            val view = activity.layoutInflater.inflate(R.layout.item_way_two, null)
            footerLayout.addView(view, footerLayout.childCount - 1)
            take(footerLayout.childCount - 2, null)
        }
        initClicklistener()
    }

    private fun addPointExisting(point: WayInfo.Point) {
        val view = activity.layoutInflater.inflate(R.layout.item_way_two, null)
        footerLayout.addView(view, footerLayout.childCount - 1)
        take(footerLayout.childCount - 2, point)
    }

    fun setInfoByEdit(wayInfo: WayInfo) {
        edit_name.setText(wayInfo.wayName)
        kindtag = wayInfo.wayKind!!
        edit_content.setText(wayInfo.wayContent)
        for (i in kindsArray.indices) {
            if (kindsArray[i] == kindtag) {
                segment.setSelectedIndex(i)
                break
            }
        }
        text_show_location.text = wayInfo.wayLocation
        if (wayInfo.wayPointList!!.size > 0) {
            val list = wayInfo.wayPointList
            for (i in list!!.indices) {
                addPointExisting(list[i])
            }
        }
    }

    private fun getLocation() {
        val intent = Intent(activity, PickLocationActivity::class.java)
        activity.startActivityForResult(intent, 0)
    }

    fun setLocation(location: String) {
        this.location = location
        text_show_location.text = location
    }

    fun collecInfo(): Boolean {
        if (getWayName() == null || getWayLocation() == null || getWayContent() == null) {
            return false
        } else {
            wayinfo.wayName = getWayName()
            wayinfo.wayContent = getWayContent()
            wayinfo.wayLocation = getWayLocation()
            wayinfo.wayKind = getWayKind()
            val list = ArrayList<WayInfo.Point>()
            var m = footerLayout.childCount - 2
            while (m >= 0) {
                val myview = viewholderList[m]
                if (myview.getPointName() == null) {
                    return false
                } else {
                    val point = WayInfo.Point()
                    point.pointName = myview.getPointName()
                    point.pointContent = myview.getPointContent()
                    point.pointPic = myview.getPointPic()
                    list.add(point)
                }
                m--
            }
            wayinfo.wayPointList = list
            return true
        }
    }

    private fun getWayName(): String? {
        return if (edit_name.text.toString() == "") {
            Toast.makeText(activity, "线路名称不能为空！", Toast.LENGTH_SHORT).show()
            null
        } else {
            edit_name.text.toString()
        }
    }

    private fun getWayLocation(): String? {
        return if (text_show_location.text.toString() == "") {
            Toast.makeText(activity, "线路位置不能为空！", Toast.LENGTH_SHORT).show()
            null
        } else {
            text_show_location.text.toString()
        }
    }

    private fun getWayKind(): String {
        return kindtag
    }

    private fun getWayContent(): String? {
        return if (edit_content.text.toString() == "") {
            Toast.makeText(activity, "线路详细描述不能为空！", Toast.LENGTH_SHORT).show()
            null
        } else {
            edit_content.text.toString()
        }
    }

    fun getWayInfo(): WayInfo {
        return wayinfo
    }

    fun setPic(uri: Uri) {
        val viewholder = viewholderList[tag_pic_postion]
        viewholder.addUri(uri)
    }

    private fun take(position: Int, point: WayInfo.Point?) {
        val view = footerLayout.getChildAt(position)
        val point_name: EditText = view.findViewById(R.id.edit_text_point_name)
        val point_content: EditText = view.findViewById(R.id.edit_text_point_content)
        val point_image: MultiPictureView = view.findViewById(R.id.multiple_image_point)
        val viewholder = myViewHolder(position, point_name, point_content, point_image, activity)
        if (point != null) {
            point_name.setText(point.pointName)
            point_content.setText(point.pointContent)
            if (point.pointPic != null) point_image.addItem(Uri.parse(point.pointPic))
        }
        viewholderList.add(viewholder)
    }

    inner class myViewHolder {
        var position: Int? = null
        var name: EditText? = null
        var content: EditText? = null
        var image: MultiPictureView? = null
        private var activity: Activity

        constructor(position1: Int, name: EditText?, content: EditText?, image: MultiPictureView?, activity: Activity) {
            position = position1
            this.name = name
            this.content = content
            this.image = image
            this.activity = activity
            initMutiplePicView()
        }

        private fun initMutiplePicView() {
            image!!.addClickCallback = object : MultiPictureView.AddClickCallback {
                override fun onAddClick(view: View) {
                    tag_pic_postion = position!!
                    val m = MatisseUtil(activity)
                    m.startPoint()
                }
            }
        }

        fun addUri(uri: Uri) {
            image!!.addItem(uri)
        }

        fun getPointName(): String? {
            return if (name!!.text.toString() == "") {
                Toast.makeText(activity, "节点名称不能为空！", Toast.LENGTH_SHORT).show()
                null
            } else {
                name!!.text.toString()
            }
        }

        fun getPointContent(): String? {
            return if (content!!.text.toString() == "") {
                null
            } else {
                content!!.text.toString()
            }
        }

        fun getPointPic(): String? {
            return if (image!!.getList().size > 0) {
                image!!.getList()[0].toString()
            } else {
                null
            }
        }
    }
}

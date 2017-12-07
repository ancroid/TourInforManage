package com.newth.tourinformanage.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.newth.tourinformanage.R
import com.newth.tourinformanage.activity.ShowSceneActivity
import com.newth.tourinformanage.adapter.SceneInfoAdapter
import com.newth.tourinformanage.bean.SceneInfo
import com.newth.tourinformanage.dbHelper.SceneInfoDB
import android.content.DialogInterface


/**
 * Created by Mr.chen on 2017/11/17.
 */
class ScenInfoFragment : Fragment() {

    private lateinit var toolbar: Toolbar
    private lateinit var reclerview: RecyclerView
    private lateinit var swipeRefesh: SwipeRefreshLayout
    private var db: SceneInfoDB = SceneInfoDB.get()
    private var dataList = ArrayList<SceneInfo>()
    private lateinit var myAdapter: SceneInfoAdapter

    companion object {
        fun newInstance(): ScenInfoFragment = ScenInfoFragment()
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_scenery_info, container, false)
        toolbar = view.findViewById(R.id.tool_bar)
        reclerview = view.findViewById(R.id.recycler_sceneinfo)
        swipeRefesh = view.findViewById(R.id.swipe_scene_info)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
        querySceneInfo()
    }

    private fun initview() {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        val actionbar = activity.supportActionBar
        if (actionbar != null) {
            actionbar.title = "景点信息管理"
            actionbar.setDisplayHomeAsUpEnabled(false)
        }
        swipeRefesh.setOnRefreshListener {
            querySceneInfo()
        }
    }

    private fun initRecycler() {
        if (swipeRefesh.isRefreshing) {
            swipeRefesh.isRefreshing = false
        }
        if (dataList.size > 0) {
            myAdapter = SceneInfoAdapter(dataList)
            val linerManager = LinearLayoutManager(activity)
            reclerview.layoutManager = linerManager
            reclerview.adapter = myAdapter
            myAdapter.setOnItemClickListener { adapter, view, position ->
                val intent = Intent(activity, ShowSceneActivity::class.java)
                intent.putExtra("id", dataList[position].id)
                activity.startActivity(intent)
            }
            myAdapter.setOnItemLongClickListener { adapter, view, position ->
                AlertDialog.Builder(activity)
                        .setTitle("Reminding")
                        .setMessage("Are you sure to delete?")
                        .setPositiveButton("Yes", { idialog, which ->
                            deleteSceneByID(dataList[position].id!!)
                        })
                        .setNegativeButton("No", { idialog, which ->
                            Toast.makeText(activity, "已取消", Toast.LENGTH_SHORT).show()
                        })
                        .show()
                true
            }
        } else {
            Toast.makeText(activity, "no info", Toast.LENGTH_SHORT).show()
        }
    }

    private fun querySceneInfo() {
        dataList = db.getSceneInfo()
        initRecycler()
    }

    private fun deleteSceneByID(id: Int) {
        if (db.deleteSceneByID(id)) {
            dataList = db.getSceneInfo()
            setData()
        }
    }

    private fun setData() {
        myAdapter.setNewData(dataList)
    }
}
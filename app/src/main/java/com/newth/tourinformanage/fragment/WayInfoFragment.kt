package com.newth.tourinformanage.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
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
import com.newth.tourinformanage.activity.ShowWayActivity
import com.newth.tourinformanage.adapter.WayFragmentAdapter
import com.newth.tourinformanage.bean.WayInfo
import com.newth.tourinformanage.dbHelper.WayInfoDB

/**
 * Created by Mr.chen on 2017/12/6.
 */

class WayInfoFragment : Fragment() {
    private lateinit var toolbar: Toolbar
    private lateinit var reclerview: RecyclerView
    private lateinit var swipeRefesh: SwipeRefreshLayout
    private var db: WayInfoDB = WayInfoDB.get()
    private var dataList = ArrayList<WayInfo>()
    private lateinit var myAdapter:WayFragmentAdapter
    companion object {
        fun newInstance(): WayInfoFragment {
            return WayInfoFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_way_info, container, false)
        toolbar = view.findViewById(R.id.tool_bar)
        reclerview = view.findViewById(R.id.recycler_wayinfo)
        swipeRefesh = view.findViewById(R.id.swipe_way_info)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
        queryWayInfo()
    }

    private fun initview() {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        val actionbar = activity.supportActionBar
        if (actionbar != null) {
            actionbar.title = "旅游线路管理"
            actionbar.setDisplayHomeAsUpEnabled(false)
        }
        swipeRefesh.setOnRefreshListener {
            queryWayInfo()
        }
    }

    private fun initRecycler() {
        if (swipeRefesh.isRefreshing) {
            swipeRefesh.isRefreshing = false
        }
        if (dataList.size > 0) {
            myAdapter = WayFragmentAdapter(dataList)
            val linerManager = LinearLayoutManager(activity)
            reclerview.layoutManager = linerManager
            reclerview.adapter = myAdapter
            myAdapter.setOnItemClickListener { adapter, view, position ->
                val intent = Intent(activity, ShowWayActivity::class.java)
                intent.putExtra("id", dataList[position].id)
                activity.startActivity(intent)
            }
            myAdapter.setOnItemLongClickListener { adapter, view, position ->
                AlertDialog.Builder(activity)
                        .setTitle("Reminding")
                        .setMessage("Are you sure to delete?")
                        .setPositiveButton("Yes", { idialog, which ->
                            deleteWay(dataList[position].id!!)
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
    private fun queryWayInfo() {
        dataList = db.getonlyWayInfo()
        initRecycler()
    }
    private fun deleteWay(id:Int){
        db.deleteWayInfo(id,db.isHavePoint(id))
        dataList=db.getonlyWayInfo()
        setData()
    }

    private fun setData() {
        myAdapter.setNewData(dataList)
    }
}

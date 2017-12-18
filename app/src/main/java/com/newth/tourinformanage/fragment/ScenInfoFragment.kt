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
import android.util.Log
import android.view.*
import android.widget.Toast
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.newth.tourinformanage.R
import com.newth.tourinformanage.activity.ShowSceneActivity
import com.newth.tourinformanage.adapter.SceneInfoAdapter
import com.newth.tourinformanage.bean.SceneInfo
import com.newth.tourinformanage.dbHelper.SceneInfoDB


/**
 * Created by Mr.chen on 2017/11/17.
 */
class ScenInfoFragment : Fragment() {

    private lateinit var toolbar: Toolbar
    private lateinit var reclerview: RecyclerView
    private lateinit var swipeRefesh: SwipeRefreshLayout
    private lateinit var searchview: MaterialSearchView

    private var db: SceneInfoDB = SceneInfoDB.get()
    private var dataList = ArrayList<SceneInfo>()
    private lateinit var myAdapter: SceneInfoAdapter

    companion object {
        fun newInstance(): ScenInfoFragment = ScenInfoFragment()
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_scenery_info, container, false)
        toolbar = view.findViewById(R.id.tool_bar)
        reclerview = view.findViewById(R.id.recycler_sceneinfo)
        swipeRefesh = view.findViewById(R.id.swipe_scene_info)
        searchview = view.findViewById(R.id.search_view)
        searchview.visibility=View.VISIBLE
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
            if (searchview.isSearchOpen){
                searchview.closeSearch()
            }
            querySceneInfo()
        }
        searchview.setOnQueryTextListener(object :MaterialSearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                querySceneInfoByName(query!!)
                Log.d("mmm",query)
                return false
            }
        })
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity.menuInflater.inflate(R.menu.menu_search, menu)
        val item = menu!!.findItem(R.id.action_search)
        searchview.setMenuItem(item)
    }



    private fun querySceneInfo() {
        dataList = db.getSceneInfo()
        initRecycler()
    }
    private fun querySceneInfoByName(name:String){
        val list=db.getSceneInfoByName(name)
        if (list.size>0){
            dataList=list
            setData()
        }else{
            Toast.makeText(activity,"无该景点信息",Toast.LENGTH_SHORT).show()
        }
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
package com.newth.tourinformanage.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.widget.FrameLayout
import android.widget.Toast
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.newth.tourinformanage.R
import com.newth.tourinformanage.fragment.ScenInfoFragment
import com.newth.tourinformanage.fragment.TourGuideFragment
import com.newth.tourinformanage.fragment.WayInfoFragment

class MainActivity : AppCompatActivity() {

    private lateinit var framelayout: FrameLayout
    private lateinit var navigationbar: BottomNavigationBar
    private lateinit var floatbutton: FloatingActionButton

    private lateinit var fragmentManager: FragmentManager
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        framelayout = findViewById(R.id.frame_main)
        navigationbar = findViewById(R.id.navigation_bar_main)
        floatbutton = findViewById(R.id.float_add)
        getPermission()
    }

    private fun initview() {
        floatbutton.setOnClickListener {
            when (currentPosition) {
                0 -> {
                    val intent = Intent(this, AddScenInfoActivity::class.java)
                    startActivity(intent)
                }
                1 -> {
                    val intent = Intent(this, AddTourWayActivity::class.java)
                    startActivity(intent)
                }
                2 -> {
                    val intent = Intent(this, AddTourGuideActivity::class.java)
                    startActivity(intent)
                }
            }

        }
        initBottomBar()
        initFragment()
    }

    private fun initBottomBar() {
        navigationbar.activeColor = R.color.primary_color
        navigationbar.addItem(BottomNavigationItem(R.drawable.ic_scene, "景点"))
                .addItem(BottomNavigationItem(R.drawable.ic_way, "线路"))
                .addItem(BottomNavigationItem(R.drawable.ic_guide, "导游"))
                .initialise()
        navigationbar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                changeFragment(position)
            }

            override fun onTabUnselected(position: Int) {

            }

            override fun onTabReselected(position: Int) {

            }
        })
    }

    private fun initFragment() {
        fragmentManager = supportFragmentManager
        changeFragment(0)
    }

    private fun changeFragment(position: Int) {
        val transaction = fragmentManager.beginTransaction()
        if (position != currentPosition) {
            val oldfragment = fragmentManager.findFragmentByTag(makeTag(currentPosition))
            if (oldfragment != null) {
                transaction.hide(oldfragment)
            }
            currentPosition = position
        }
        val newfragment = fragmentManager.findFragmentByTag(makeTag(position))
        if (newfragment != null) {
            transaction.show(newfragment)
        } else {
            transaction.add(R.id.frame_main, makeFragment(position), makeTag(position))
        }
        transaction.commitAllowingStateLoss()
    }

    private fun makeFragment(position: Int): Fragment = when (position) {
        0 -> ScenInfoFragment.newInstance()
        1 -> WayInfoFragment.newInstance()
        2 -> TourGuideFragment.newInstance()
        else -> ScenInfoFragment.newInstance()
    }

    private fun makeTag(position: Int): String = ((R.id.frame_main) + position).toString()

    private fun getPermission(){
        when {
            ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
            ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
            else -> initview()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            1->{
                if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    initview()
                }else{
                    Toast.makeText(this,"you denied permissions",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}

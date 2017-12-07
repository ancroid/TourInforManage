package com.newth.tourinformanage.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.widget.FrameLayout
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.newth.tourinformanage.R
import com.newth.tourinformanage.fragment.ScenInfoFragment
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
        initview()
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
            }

        }
        initBottomBar()
        initFragment()
    }

    private fun initBottomBar() {
        navigationbar.activeColor = R.color.primary_color
        navigationbar.addItem(BottomNavigationItem(R.mipmap.ic_launcher, "1"))
                .addItem(BottomNavigationItem(R.mipmap.ic_launcher, "2"))
                .addItem(BottomNavigationItem(R.mipmap.ic_launcher, "3"))
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
        else -> ScenInfoFragment.newInstance()
    }

    private fun makeTag(position: Int): String = ((R.id.frame_main) + position).toString()

}

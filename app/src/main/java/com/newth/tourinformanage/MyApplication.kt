package com.newth.tourinformanage

import android.app.Application
import android.content.Context

/**
 * Created by Mr.chen on 2017/11/29.
 */
class MyApplication : Application() {
    companion object {
        lateinit var context: Context
        fun getAppContext(): Context = context
    }


    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}
package com.newth.tourinformanage.util

import android.app.Activity
import android.content.pm.ActivityInfo
import com.newth.tourinformanage.R
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.PicassoEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy

/**
 * Created by Mr.chen on 2017/11/27.
 */
class MatisseUtil constructor(private val activity: Activity) {

    companion object {
        val REQUEST_CODE_CHOOSE = 1
    }
    fun start(){
        Matisse.from(activity)
                .choose(MimeType.allOf())
                .capture(true)
                .captureStrategy( CaptureStrategy(true,"com.newth.tourinformanage.fileprovider"))
                .countable(true)
                .maxSelectable(6)
                .gridExpectedSize(activity.resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(PicassoEngine())
                .forResult(REQUEST_CODE_CHOOSE)
    }
    fun startPoint(){
        Matisse.from(activity)
                .choose(MimeType.allOf())
                .capture(true)
                .captureStrategy( CaptureStrategy(true,"com.newth.tourinformanage.fileprovider"))
                .countable(true)
                .maxSelectable(1)
                .gridExpectedSize(activity.resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(PicassoEngine())
                .forResult(REQUEST_CODE_CHOOSE)
    }
}

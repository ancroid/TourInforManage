package com.newth.tourinformanage.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.newth.tourinformanage.R
import com.newth.tourinformanage.bean.SceneInfo

/**
 * Created by Mr.chen on 2017/12/3.
 */

class SceneInfoAdapter(data: List<SceneInfo>?) : BaseQuickAdapter<SceneInfo, BaseViewHolder>(R.layout.item_sceneinfo, data) {

    override fun convert(helper: BaseViewHolder, item: SceneInfo) {
        helper.setText(R.id.text_scene_name,item.sceneName)
                .setText(R.id.text_scene_kind,item.sceneKind)
                .setText(R.id.text_location_show,item.sceneLocation)
                .setText(R.id.text_scene_content,item.sceneContent)
    }
}

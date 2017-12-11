package com.newth.tourinformanage.adapter

import android.net.Uri
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.newth.tourinformanage.MyApplication
import com.newth.tourinformanage.R
import com.newth.tourinformanage.bean.GuideInfo
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Mr.chen on 2017/12/10.
 */

class TourGuideAdapter(data: List<GuideInfo>?) : BaseQuickAdapter<GuideInfo, BaseViewHolder>(R.layout.item_guide_info,data) {

    override fun convert(helper: BaseViewHolder, item: GuideInfo) {
        Picasso.with(MyApplication.getAppContext())
                .load(Uri.parse(item.guidePort))
                .fit()
                .error(R.drawable.ic_defeat)
                .into(helper.getView<CircleImageView>(R.id.img_guide))
        helper.setText(R.id.text_guide_name,item.guideName)
                .setText(R.id.text_guide_gender,item.guideGender)
                .setText(R.id.text_guide_age,item.guideAge)
                .setText(R.id.text_guide_target_tour,item.guideTargetTour)
    }
}

package com.newth.tourinformanage.adapter

import android.net.Uri
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goyourfly.multi_picture.MultiPictureView
import com.newth.tourinformanage.R
import com.newth.tourinformanage.bean.WayMutipleItem
import java.util.ArrayList

/**
 * Created by Mr.chen on 2017/12/7.
 */

class ShowWayAdapter
/**
 * Same as QuickAdapter#QuickAdapter(Context,int) but with
 * some initialization data.
 *
 * @param data A new list is created out of this one to avoid mutable list
 */
(data: ArrayList<WayMutipleItem>) : BaseMultiItemQuickAdapter<WayMutipleItem, BaseViewHolder>(data) {

    init {
        addItemType(WayMutipleItem.ONE, R.layout.item_way_show_one)
        addItemType(WayMutipleItem.TWO, R.layout.item_way_show_two)
    }

    override fun convert(helper: BaseViewHolder, item: WayMutipleItem) {
        when (helper.itemViewType) {
            1 -> {
                val wayinfo=item.wayInfo
                if (wayinfo != null) {
                    helper.setText(R.id.text_way_name, wayinfo.wayName)
                            .setText(R.id.text_location_show,wayinfo.wayLocation)
                            .setText(R.id.text_way_content,wayinfo.wayContent)
                }
            }
            2 -> {
                val point=item.point
                if (point!=null){
                    helper.setText(R.id.text_point_name,point.pointName)
                            .setText(R.id.text_point_content,point.pointContent)
                            .setText(R.id.text_point_count,"节点"+(helper.adapterPosition))
                    if(point.pointPic!=null){
                        val view=helper.getView<MultiPictureView>(R.id.img_point)
                        view.addItem(Uri.parse(point.pointPic))
                    }
                }
            }
        }
    }
}

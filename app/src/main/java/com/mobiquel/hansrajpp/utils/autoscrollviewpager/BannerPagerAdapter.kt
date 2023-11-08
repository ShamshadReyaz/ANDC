package com.mobiquel.hansrajpp.utils.autoscrollviewpager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.mobiquel.hansrajapp.R

class BannerPagerAdapter(private val bannerList: List<Int>?) : PagerAdapter() {
    private var context: Context? = null
    override fun getCount(): Int {
        return bannerList?.size ?: 0
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        context = container.context
        val view =
            LayoutInflater.from(context).inflate(R.layout.layout_banner, null)
        val bannerImg: ImageView = view.findViewById(R.id.sdv_banner)
        val label = view.findViewById<TextView>(R.id.label)
        val label2 = view.findViewById<TextView>(R.id.label2)
        //context?.setImageGlide(bannerImg, bannerList!![position])
        Glide.with(context!!)
            .load(bannerList!![position])
            .into(bannerImg)
        //label.text = "LEH DISTRICT\nTOURIST MANAGEMENT SYSTEM"
        //label.textSize = 20f
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun destroyItem(
        container: View,
        position: Int,
        `object`: Any
    ) {
        (container as AutoScrollViewPager).removeView(`object` as View)
    }

}
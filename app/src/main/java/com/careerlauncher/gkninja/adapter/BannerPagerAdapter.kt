package com.careerlauncher.gkninja.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.careerlauncher.gkninja.R
import com.careerlauncher.gkninja.utils.AutoScrollViewPager
import com.careerlauncher.gkninja.utils.setImageGlide

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
        //String url=bannerList.get(position).replace("https","http" ).replaceAll(" ","%20");
        context?.setImageGlide(bannerImg, bannerList!![position])
        if (position == 0) {
            label.text = "Play and Earn"
            label.textSize = 25f
        } else if (position == 1) {
            label.textSize = 18f
            label.text = "Rank among 10,000 players world wide"
        } else if (position == 2) {
            label.textSize = 18f
            label.text =
                "Questions from categories like Ancient India, Medieval India,Science, Sports,Sobriquets etc."
        } else if (position == 3) {
            label.textSize = 18f
            label.text =
                "Game adjusts the difficulty level depending on your responses, making GK a fun activity all the time"
        }


        //Log.e("URL", url);
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
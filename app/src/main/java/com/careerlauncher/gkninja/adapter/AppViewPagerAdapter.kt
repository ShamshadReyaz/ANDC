package com.careerlauncher.gkninja.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.careerlauncher.gkninja.pojo.ViewPagerModel

class AppViewPagerAdapter(
    fragmentList: List<ViewPagerModel>?,
    fm: FragmentManager?
) : FragmentStatePagerAdapter(fm!!) {
    private val fragmentList: List<ViewPagerModel>?
    override fun getItem(position: Int): Fragment {
        return fragmentList!![position].fragment
    }

    override fun getCount(): Int {
        return fragmentList?.size ?: 0
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentList!![position].title
    }

    init {
        this.fragmentList = fragmentList
    }
}
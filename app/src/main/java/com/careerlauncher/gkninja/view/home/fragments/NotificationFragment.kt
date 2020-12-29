package com.careerlauncher.gkninja.view.home.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.careerlauncher.gkninja.R
import com.careerlauncher.gkninja.base.BaseFragment
import com.careerlauncher.gkninja.base.BaseView

/**
 * Created by Arman Reyaz on 12/19/2020.
 */
class NotificationFragment : BaseFragment(),BaseView {

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_notification,container,false)
        
    }
    override fun initVariables() {
    }

    override fun setListeners() {
    }

    override fun showSnackBar(message: String?, view: View?) {
    }
}
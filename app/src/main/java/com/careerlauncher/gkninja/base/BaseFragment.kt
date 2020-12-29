package com.careerlauncher.gkninja.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.careerlauncher.gkninja.pojo.FailureResponse

/**
 * Created by Navjot Singh
 * on 2/3/19.
 * this Fragment class is the parent class for all the fragments in the application
 * it contains methods to be used by child fragments
 */
open abstract class BaseFragment : Fragment(), BaseView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVariables()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    abstract fun initVariables()
    abstract fun setListeners()
    val isFragmentAdded: Boolean
        get() = requireActivity() != null && !requireActivity().isFinishing && isAdded

    override fun showNoNetworkError() {
        if (isFragmentAdded) (activity as BaseActivity?)!!.showNoNetworkError()
    }

    override fun showProgressBar() {
        if (isFragmentAdded) (activity as BaseActivity?)!!.showProgressBar()
    }

    fun showSnackBar(message:String) {
        if (isFragmentAdded) (activity as BaseActivity?)!!.showSnackBar(message)
    }

    override fun hideProgressBar() {
        if (isFragmentAdded) {
            (activity as BaseActivity?)!!.hideProgressBar()
        }
    }

    override fun showSpecificError(failureResponse: FailureResponse?) {
        if (isFragmentAdded) {
            (activity as BaseActivity?)!!.showSpecificError(failureResponse)
        }
    }

    override val isNetworkAvailable: Boolean
        get() = isFragmentAdded && (activity as BaseActivity?)!!.isNetworkAvailable
}
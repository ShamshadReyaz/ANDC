package com.careerlauncher.gkninja.base

import com.careerlauncher.gkninja.pojo.FailureResponse
import java.lang.ref.SoftReference

/**
 * Created by Navjot Singh
 * on 2/3/19.
 */
abstract class BasePresenter<T : BaseView?>(view: T) : BaseModelListener {
    private var view: SoftReference<T>? = null
    fun attachView(view: T) {
        this.view = SoftReference(view)
    }

    fun getView(): T? {
        return if (view == null) null else view!!.get()
    }

    fun detachView() {
        view = null
        destroy()
    }

    protected abstract fun setModel()
    protected abstract fun destroy()
    protected abstract fun initView()

    /**
     * Common place to receive noNetwork hook
     * this can be passed on to BaseFragment or BaseActivity to show common screen or error
     */
    override fun noNetworkError() {
        if (getView() != null) {
            getView()!!.hideProgressBar()
            getView()!!.showNoNetworkError()
        }
    }

    /**
     * Common place to log errors to Analytics or custom file logging
     * Every Presenter can override this method to provide custom handling if required
     */
    override fun onErrorOccurred(baseResponse: FailureResponse?) {
        if (getView() != null) {
            getView()!!.hideProgressBar()
            getView()?.showSpecificError(baseResponse)
        }
    }

    init {
        attachView(view)
        setModel()
        initView()
    }
}
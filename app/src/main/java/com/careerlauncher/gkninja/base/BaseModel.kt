package com.careerlauncher.gkninja.base

import com.careerlauncher.gkninja.data.DataManager
import com.careerlauncher.gkninja.network.CommonResponseHandler
import java.lang.ref.SoftReference

abstract class BaseModel<T : BaseModelListener?>(listener: T) : CommonResponseHandler {
    var listener: SoftReference<T>?
    fun attachListener(listener: T) {
        this.listener = SoftReference(listener)
    }

    fun detachListener() {
        listener = null
    }

    fun getListener(): T? {
        return if (listener != null) listener!!.get() else null
    }

    abstract fun init()
    val dataManager: DataManager
        get() = DataManager.instance!!

    override fun onNetworkError() {
        if (getListener() != null) getListener()!!.noNetworkError()
    }

    companion object {
        private const val NO_NETWORK = 999
    }

    init {
        this.listener = SoftReference(listener)
    }
}
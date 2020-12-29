package com.careerlauncher.gkninja.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.OnClick
import com.careerlauncher.gkninja.R
import com.careerlauncher.gkninja.interfaces.DialogListener
import kotlinx.android.synthetic.main.activity_signup.view.*

class UpdateAppDialog(
    private val mContext: Context,
    private val dialogListener: DialogListener?) : Dialog(mContext) {
    var message: TextView? = null
    var update: TextView? = null
    var accept: CheckBox? = null
    var close: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_forgot_username)
        update=findViewById(R.id.cancel)
        message=findViewById(R.id.message)
        accept=findViewById(R.id.checkbox)
        close=findViewById(R.id.close)

        window?.setDimAmount(0.5f)
        window?.setBackgroundDrawable(null)
        window?.attributes?.windowAnimations = R.style.DialogBounceAnimation
        window?.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)
        setCanceledOnTouchOutside(true)
        update?.setOnClickListener {
            dialogListener?.onPositiveButtonClick()
        }
        close?.setOnClickListener {
            dialogListener?.onNegativeButtonClick()
        }

    }


    fun setData(messageValue: String?) {
        message?.text = messageValue
        update?.text = "Cancel"
    }

    fun setAppUpdateData(messageValue: String, aboutAppUpdate: String) {
        if (aboutAppUpdate == "") message?.text = messageValue else {
            message?.text = "$messageValue\nThis update contains:\n$aboutAppUpdate"
        }
        close?.text = "Cancel"
        close?.visibility=View.VISIBLE
        close?.setTextColor(Color.parseColor("#000000"))
        accept?.text = "Don't show this popup until next update!"
    }

    fun hideCheckBox() {
        accept?.visibility = View.GONE
    }

    val checked: Boolean
        get() = if (accept!!.isChecked) true else false

    fun setData2(messageValue: String?) {
        close?.visibility = View.VISIBLE
        update?.text = "YES"
        close?.text = "NO"
        message?.text = messageValue
    }

    fun setButtonName(messageValue: String?) {
        update?.text = messageValue
    }

}
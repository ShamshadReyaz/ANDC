package com.careerlauncher.gkninja.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.careerlauncher.gkninja.R
import com.careerlauncher.gkninja.interfaces.DialogListener

class EndQuizConfirmationDialog(private val mContext: Context, private val dialogListener: DialogListener) : Dialog(mContext) {

    var tvAttempts: TextView? = null
    var tv_label_submit_test: TextView? = null
    var tv_confirmation: TextView? = null
    var btnSubmit: Button? = null
    var btnGoBack: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_end_quiz_confirmation)
        tvAttempts = findViewById(R.id.tv_attempts)
        tv_label_submit_test = findViewById(R.id.tv_label_submit_test)
        tv_confirmation = findViewById(R.id.tv_confirmation)
        btnSubmit = findViewById(R.id.btn_submit)
        btnGoBack = findViewById(R.id.btn_go_back)


        window!!.setDimAmount(0.5f)
        window!!.setBackgroundDrawable(null)
        window!!.attributes.windowAnimations = R.style.DialogBounceAnimation
        window!!.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        window!!.setGravity(Gravity.CENTER)
        setCanceledOnTouchOutside(true)
        btnSubmit?.setOnClickListener {
            dialogListener.onPositiveButtonClick()
        }
        btnGoBack?.setOnClickListener {
            dialogListener.onNegativeButtonClick()
        }
    }

    fun setAttempts(attempts: String?) {
        tvAttempts!!.text = attempts
    }

    fun setVisibility() {
        tvAttempts!!.visibility = View.VISIBLE
    }

    fun setData(s1: String?, s2: String?) {
        tv_label_submit_test?.text = s1
        tv_confirmation?.text = s2
    }

    fun setPositiveButtonText(attempts: String?) {
        btnSubmit?.text = attempts
    }

    fun setNegativeButtonText(attempts: String?) {
        btnGoBack?.text = attempts
    }

    fun setBookData() {
        tv_label_submit_test?.text = "Book Slot Confirmation"
        tv_confirmation?.text = "Are you sure you want to book this slot?"
        btnSubmit?.text = "BOOK"
        btnGoBack?.text = "CANCEL"
    }

}
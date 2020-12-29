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

class StreakCompletedDialog(
    private val mContext: Context,
    private val dialogListener: DialogListener) : Dialog(mContext) {
    var pointsScored: TextView? = null
    var btnRestart: Button? = null
    var btnClose: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_streak_complete)
        pointsScored=findViewById(R.id.points)
        btnRestart=findViewById(R.id.btn_restart)
        btnClose=findViewById(R.id.btn_close)

        window!!.setDimAmount(0.5f)
        window!!.setBackgroundDrawable(null)
        window!!.attributes.windowAnimations = R.style.DialogBounceAnimation
        window!!.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        window!!.setGravity(Gravity.CENTER)
        //setCancelable(false);
        setCanceledOnTouchOutside(false)

        btnRestart?.setOnClickListener {
            dialogListener.onPositiveButtonClick()
        }
        btnClose?.setOnClickListener {
            dialogListener.onNegativeButtonClick()
        }
    }

    fun setLabel(points: String?) {
        pointsScored!!.text = points
    }

}
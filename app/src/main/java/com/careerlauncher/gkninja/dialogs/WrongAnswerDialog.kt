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

class WrongAnswerDialog(private val mContext: Context, private val dialogListener: DialogListener) : Dialog(mContext) {

    var tvQuestion: TextView? = null
    var tvOptionNumber: TextView? = null
    var tvAnswer: TextView? = null
    var tvLabelGameOver: TextView? = null
    var subTitle: TextView? = null
    var pointsScored: TextView? = null
    var streakText: TextView? = null
    var tvLabelCorrectOption: TextView? = null
    var btnRestart: Button? = null
    var btnClose: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_wrong_answers)

        btnClose=findViewById(R.id.btn_close)
        btnRestart=findViewById(R.id.btn_restart)
        tvLabelCorrectOption=findViewById(R.id.tv_label_correct_option)
        streakText=findViewById(R.id.streakText)
        pointsScored=findViewById(R.id.points)
        subTitle=findViewById(R.id.subTitle)
        tvLabelGameOver=findViewById(R.id.tv_label_game_over)
        tvAnswer=findViewById(R.id.tv_answer)
        tvOptionNumber=findViewById(R.id.tv_option_number)
        tvQuestion=findViewById(R.id.tv_question)


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


    fun setLabel(
        title: String?,
        label: String?,
        points: String?,
        streak: String,
        optioNUmber: String?,
        answer: String?,
        question: String?
    ) {
        tvLabelGameOver!!.text = title
        subTitle!!.text = label
        pointsScored!!.text = points
        streakText!!.text = "Your streak lasted $streak questions."
        tvOptionNumber!!.text = optioNUmber
        tvAnswer!!.text = answer
        tvQuestion!!.text = question
    }

}
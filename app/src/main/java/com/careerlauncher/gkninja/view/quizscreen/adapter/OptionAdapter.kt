package com.careerlauncher.gkninja.view.quizscreen.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.careerlauncher.gkninja.R
import com.careerlauncher.gkninja.interfaces.RecyclerItemClickListener
import com.careerlauncher.gkninja.pojo.OptionsModel
import kotlinx.android.synthetic.main.list_item_option.view.*
import java.util.*


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class OptionAdapter(
    var context: Context,
    private var mOptionsList: ArrayList<OptionsModel>,
    var recyclerItemClickListener: RecyclerItemClickListener
) : RecyclerView.Adapter<OptionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_option, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mOptionsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hobby = mOptionsList[position]
        holder.setData(hobby, position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentRankModel: OptionsModel? = null
        var currentPosition: Int = 0

        @SuppressLint("ResourceType")
        fun setData(rankModel: OptionsModel?, i: Int) {
            rankModel?.let {
                itemView.tv_option.setText(mOptionsList.get(i).option)
                itemView.tv_option_no.setText((i + 1).toString() + "")
                // itemView.ll.setCurrentlySelected(mOptionsList.get(i).isCurrentlySelected());
                // itemView.ll.setCurrentlySelected(mOptionsList.get(i).isCurrentlySelected());
                val corrcetAnswer: Drawable =
                    context.getResources().getDrawable(R.drawable.ic_correct_circle)
                val wrongAnswer: Drawable =
                    context.getResources().getDrawable(R.drawable.ic_incorrect_wite)

                if (mOptionsList.get(i).isNeutral && !mOptionsList.get(i).isSelected) {
//simplebackground
                    itemView.ll.setBackgroundResource(R.drawable.shape_rect_gray_stroke_options_question)
                    itemView.tv_option.setTextColor(Color.parseColor("#FFFFFF"))
                    itemView.tv_option_no.setTextColor(Color.parseColor("#FFFFFF"))
                    itemView.tv_option.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        null,
                        null
                    )
                } else if (mOptionsList.get(i).isNeutral && mOptionsList.get(i).isSelected) {
                    itemView.ll.setBackgroundResource(R.drawable.shape_rect_green_stroke_options)
                    itemView.tv_option.setTextColor(
                        Color.parseColor(
                            context.getResources().getString(R.color.option_green)
                        )
                    )
                    itemView.tv_option_no.setTextColor(
                        Color.parseColor(
                            context.getResources().getString(R.color.option_green)
                        )
                    )
                    itemView.tv_option.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        null,
                        null
                    )
                } else if (!mOptionsList.get(i).isNeutral && mOptionsList.get(i)
                        .isAttemptedAnswer && !mOptionsList.get(i).isCorrectAnswer
                ) {
                    //   selected and wrong
                    itemView.ll.setBackgroundResource(R.drawable.shape_rect_red_stroke_options)
                    itemView.tv_option.setTextColor(
                        Color.parseColor(
                            context.getResources().getString(R.color.colorRed)
                        )
                    )
                    itemView.tv_option_no.setTextColor(
                        Color.parseColor(
                            context.getResources().getString(R.color.colorRed)
                        )
                    )
                    itemView.tv_option.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        wrongAnswer,
                        null
                    )
                } else if (!mOptionsList.get(i).isNeutral && mOptionsList.get(i)
                        .isAttemptedAnswer && mOptionsList.get(i).isCorrectAnswer
                ) {
                    //   selected and correct
                    itemView.ll.setBackgroundResource(R.drawable.shape_rect_green_stroke_options)
                    itemView.tv_option.setTextColor(
                        Color.parseColor(
                            context.getResources().getString(R.color.option_green)
                        )
                    )
                    itemView.tv_option_no.setTextColor(
                        Color.parseColor(
                            context.getResources().getString(R.color.option_green)
                        )
                    )
                    itemView.tv_option.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        corrcetAnswer,
                        null
                    )
                } else if (mOptionsList.get(i).isNeutral && mOptionsList.get(i)
                        .isCorrectAnswer
                ) {
                    //    not selected and correct
                    itemView.ll.setBackgroundResource(R.drawable.shape_rect_green_stroke_options)
                    itemView.tv_option.setTextColor(
                        Color.parseColor(
                            context.getResources().getString(R.color.option_green)
                        )
                    )
                    itemView.tv_option_no.setTextColor(
                        Color.parseColor(
                            context.getResources().getString(R.color.option_green)
                        )
                    )
                    itemView.tv_option.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        corrcetAnswer,
                        null
                    )
                }
                itemView.ll.setSelected(mOptionsList.get(i).isSelected)
                itemView.setOnClickListener {
                    recyclerItemClickListener.onRecyclerItemClicked(i)
                }

            }

            this.currentRankModel = rankModel
            this.currentPosition = i
        }
    }
}
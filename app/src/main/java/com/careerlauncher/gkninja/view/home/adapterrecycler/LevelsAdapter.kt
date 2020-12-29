package com.careerlauncher.gkninja.view.home.adapterrecycler

import android.R.attr.data
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.careerlauncher.gkninja.R
import com.careerlauncher.gkninja.pojo.LevelsListModel
import kotlinx.android.synthetic.main.list_item_level.view.*


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class LevelsAdapter(
    var context: Context,
    private var levelList: List<LevelsListModel>,
    var points: String
) : RecyclerView.Adapter<LevelsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_level, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return levelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hobby = levelList[position]
        holder.setData(hobby, position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentRankModel: LevelsListModel? = null
        var currentPosition: Int = 0
        fun setData(rankModel: LevelsListModel?, pos: Int) {
            rankModel?.let {
                if (pos == 0) itemView.view1.setVisibility(View.INVISIBLE) else itemView.view1.setVisibility(
                    View.VISIBLE
                )

                if (pos == levelList.size - 1) itemView.view2.setVisibility(View.INVISIBLE) else itemView.view2.setVisibility(
                    View.VISIBLE
                )
                val levelPointCurrent: String =
                    levelList.get(pos).points.replace("Points", "").trim({ it <= ' ' })

                try {
                    if (levelPointCurrent.toInt() <= points.toInt()) {
                        itemView.iv_tick.setVisibility(View.VISIBLE)
                        itemView.view1.setBackgroundColor(Color.parseColor("#148323"))
                        itemView.view2.setBackgroundColor(Color.parseColor("#148323"))
                        itemView.iv_circle.setImageResource(R.drawable.drawable_level_green)
                        if (pos === 0) {
                        } else {
                            val levelPointBefore: String =
                                levelList.get(pos - 1).points.replace("Points", "").trim()

                        }
                    } else {
                        itemView.iv_tick.setVisibility(View.GONE)
                        itemView.view1.setBackgroundColor(Color.parseColor("#C5C5C5"))
                        itemView.view2.setBackgroundColor(Color.parseColor("#C5C5C5"))
                        itemView.iv_circle.setImageResource(R.drawable.drawable_level)
                    }
                } catch (e: NumberFormatException) {
                    itemView.iv_tick.setVisibility(View.GONE)
                    itemView.view1.setBackgroundColor(Color.parseColor("#C5C5C5"))
                    itemView.view2.setBackgroundColor(Color.parseColor("#C5C5C5"))
                    itemView.iv_circle.setImageResource(R.drawable.drawable_level)
                }
                itemView.tv_level_name.setText(levelList.get(pos).levelName)
                itemView.tv_points.setText(levelList.get(pos).points)

            }

            this.currentRankModel = rankModel
            this.currentPosition = pos
        }
    }
}
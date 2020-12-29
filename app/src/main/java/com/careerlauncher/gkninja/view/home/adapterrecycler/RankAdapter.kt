package com.careerlauncher.gkninja.view.home.adapterrecycler

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.careerlauncher.gkninja.R
import com.careerlauncher.gkninja.data.DataManager
import com.careerlauncher.gkninja.data.preferences.PrefKeys
import com.careerlauncher.gkninja.network.NetworkConstants
import com.careerlauncher.gkninja.pojo.RankModel
import com.careerlauncher.gkninja.utils.setImageGlide
import kotlinx.android.synthetic.main.rank_list_item.view.*

/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class RankAdapter(var context: Context, private var rankList: List<RankModel>) : RecyclerView.Adapter<RankAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rank_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rankList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hobby = rankList[position]
        holder.setData(hobby, position)
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentRankModel: RankModel? = null
        var currentPosition: Int = 0
        fun setData(rankModel: RankModel?, pos: Int) {
            rankModel?.let {
                itemView.name.text = rankModel.name

                val top = context.resources.getDrawable(R.drawable.ic_arrow_upp_rank)
                val down = context.resources.getDrawable(R.drawable.ic_arrow_down_rank)
                if (rankModel.name.equals(DataManager(context).getStringFromPreference(PrefKeys.FULL_NAME))
                ) {
                    itemView.type.setVisibility(View.GONE)
                    itemView.equalImage.setVisibility(View.VISIBLE)
                    itemView.equalImage.setImageResource(R.drawable.ic_equal_oran)
                    if (pos == rankList.size - 1) {
                        itemView.vertBottom.setVisibility(View.GONE)
                    } else {
                        itemView.vertBottom.setVisibility(View.VISIBLE)
                    }
                    itemView.vertTop.setVisibility(View.VISIBLE)
                } else {
                    if (rankModel.status.equals("downgrade")) {
                        itemView.type.setVisibility(View.VISIBLE)
                        itemView.equalImage.setVisibility(View.GONE)
                        itemView.type.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            null,
                            down
                        )
                        itemView.type.setText(rankModel.type)
                        itemView.type.setTextColor(Color.parseColor("#FF4750"))
                    } else if (rankModel.status.equals("upgrade")) {
                        itemView.type.setVisibility(View.VISIBLE)
                        itemView.equalImage.setVisibility(View.GONE)
                        itemView.type.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            top,
                            null,
                            null
                        )
                        itemView.type.setText(rankModel.type)
                        itemView.type.setTextColor(Color.parseColor("#148323"))
                    } else if (rankModel.status.equals("equalto")) {
                        itemView.type.setVisibility(View.GONE)
                        itemView.equalImage.setVisibility(View.VISIBLE)
                        itemView.equalImage.setImageResource(R.drawable.ic_equal)
                    }
                    itemView.vertTop.setVisibility(View.GONE)
                    itemView.vertBottom.setVisibility(View.GONE)
                }

                itemView.rank.setText(rankModel.rank)
                itemView.name.setText(rankModel.name)
                itemView.points.setText(rankModel.points)
                context.setImageGlide(
                    itemView.image,
                    NetworkConstants.MAIN_BASE_URL + rankModel.imaId
                )

            }

            this.currentRankModel = rankModel
            this.currentPosition = pos
        }
    }
}
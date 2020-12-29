package com.careerlauncher.gkninja.view.home.adapterrecycler

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.ahmadrosid.svgloader.SvgLoader
import com.careerlauncher.gkninja.R
import com.careerlauncher.gkninja.pojo.GameRuleListModel
import kotlinx.android.synthetic.main.cat_readiness_list_item.view.*

/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class GameRuleAdapter(var context: Context, private var gameRuleList: List<GameRuleListModel>) : RecyclerView.Adapter<GameRuleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cat_readiness_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return gameRuleList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hobby = gameRuleList[position]
        holder.setData(hobby, position)
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentRankModel: GameRuleListModel? = null
        var currentPosition: Int = 0
        fun setData(rankModel: GameRuleListModel?, pos: Int) {
            rankModel?.let {
                SvgLoader.pluck()
                    .with(context as Activity)
                    .setPlaceHolder(
                        R.drawable.placeholder_image_android,
                        R.drawable.placeholder_image_android
                    )
                    .load(gameRuleList.get(pos).iconURL, itemView.icon)
                itemView.t1.setText(
                    gameRuleList.get(pos).title?.let { it1 ->
                        HtmlCompat.fromHtml(
                            it1,
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                    }
                )

            }

            this.currentRankModel = rankModel
            this.currentPosition = pos
        }
    }
}
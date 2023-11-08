package com.mobiquel.hansrajpp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobiquel.hansrajapp.R


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class CircuitListAdapter(
    var context: Context,
    private var levelList: List<String>
) : RecyclerView.Adapter<CircuitListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_notices, parent, false)
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

        var currentRankModel: String? = null
        var currentPosition: Int = 0
        fun setData(rankModel: String?, pos: Int) {
            rankModel?.let {
              //  itemView.title.setText("\u25CF "+levelList.get(pos))
            }

            this.currentRankModel = rankModel
            this.currentPosition = pos
        }
    }
}
package com.mobiquel.srccapp.view.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobiquel.srccapp.utils.redirectToWeb2
import kotlinx.android.synthetic.main.list_item_notices.view.*
import org.json.JSONObject
import com.mobiquel.srccapp.R
import kotlinx.android.synthetic.main.list_item_maintanance.view.*


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class MaintanceListAdapter(
    var context: Context,
    private var levelList: List<String>
) : RecyclerView.Adapter<MaintanceListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_maintanance, parent, false)
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
                val jsonObject = JSONObject(levelList.get(pos))
                itemView.reqId.setText("#"+jsonObject.getString("requisitionNo"))
                itemView.category.setText("Category: "+jsonObject.getString("requsitionCategory"))
                itemView.location.setText("Location: "+jsonObject.getString("location"))
                itemView.description.setText(jsonObject.getString("description"))
                itemView.createdOn.setText(jsonObject.getString("requisitionDateTime"))

                if(jsonObject.getString("isSOSMarked").equals("T"))
                    itemView.sos.visibility=View.VISIBLE
                else
                    itemView.sos.visibility=View.GONE

            }

            this.currentRankModel = rankModel
            this.currentPosition = pos
        }
    }
}
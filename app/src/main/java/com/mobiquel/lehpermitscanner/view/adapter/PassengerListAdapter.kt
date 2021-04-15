package com.mobiquel.lehpermitscanner.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobiquel.lehpermitscanner.R
import kotlinx.android.synthetic.main.list_item_passenger.view.*
import org.json.JSONObject


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class PassengerListAdapter(
    var context: Context,
    private var levelList: List<String>
) : RecyclerView.Adapter<PassengerListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_passenger, parent, false)
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
                var j=pos+1
                var jsonObject=JSONObject(levelList.get(pos))
                itemView.messageId.setText("#"+j+" NAME: "+jsonObject.getString("name"))
                itemView.message.setText("Nationality: "+jsonObject.getString("nationality"))
                itemView.contactNumber.setText("STATE: "+jsonObject.getString("state"))
                itemView.idType.visibility=View.VISIBLE
                itemView.idType.setText("ID TYPE : "+jsonObject.getString("idType"))
                itemView.createdOn.setText("ID NO : "+jsonObject.getString("idNo"))
            }

            this.currentRankModel = rankModel
            this.currentPosition = pos
        }
    }
}
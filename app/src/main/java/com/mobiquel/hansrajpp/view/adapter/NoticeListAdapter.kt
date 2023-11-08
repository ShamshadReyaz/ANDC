package com.mobiquel.hansrajpp.view.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobiquel.hansrajpp.utils.redirectToWeb2
import kotlinx.android.synthetic.main.list_item_notices.view.*
import org.json.JSONObject
import com.mobiquel.hansrajapp.R
import com.mobiquel.hansrajpp.utils.showImagePopup


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class NoticeListAdapter(
    var context: Context,
    private var levelList: List<String>
) : RecyclerView.Adapter<NoticeListAdapter.ViewHolder>() {

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
                try{
                    val jsonObject = JSONObject(levelList.get(pos))
                    itemView.t1.setText(jsonObject.getString("title"))
                    itemView.t2.setText(jsonObject.getString("description"))
                    itemView.t4.setText(jsonObject.getString("postedBy"))
                    itemView.t5.setText(jsonObject.getString("createdOn"))

                    if (jsonObject.getString("photoURL").equals(""))
                        itemView.img.visibility = View.GONE
                    else {
                        itemView.img.visibility = View.VISIBLE
                        Glide.with(context).load(jsonObject.getString("photoURL")).into(itemView.img);

                    }
                    itemView.img.setOnClickListener {
                        context.showImagePopup(jsonObject.getString("photoURL"))
                    }
                    if (jsonObject.getString("attachmentURL").equals(""))
                        itemView.t3.visibility = View.GONE
                    else {
                        itemView.t3.visibility = View.VISIBLE
                        itemView.t3.setText("Click to View Attachment")
                        itemView.t3.setOnClickListener {
                            context.redirectToWeb2(jsonObject.getString("attachmentURL"))
                        }
                        itemView.t3.paintFlags =
                            itemView.t3.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG

                    }


                }catch (e:Exception){}

            }

            this.currentRankModel = rankModel
            this.currentPosition = pos
        }
    }
}
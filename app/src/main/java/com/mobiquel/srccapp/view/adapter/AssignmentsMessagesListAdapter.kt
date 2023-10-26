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
import com.mobiquel.srccapp.utils.showImagePopup
import org.json.JSONArray


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class AssignmentsMessagesListAdapter(
    var context: Context,
    private var levelList: JSONArray
) : RecyclerView.Adapter<AssignmentsMessagesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_assignments, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return levelList.length()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hobby = levelList.getJSONObject(position)
        holder.setData(hobby, position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(rankModel: JSONObject?, pos: Int) {
            rankModel?.let {
                val jsonObject = rankModel
                itemView.t1.setText(jsonObject.getString("facultyName"))
                itemView.t2.setText(jsonObject.getString("message"))
                itemView.t4.setText(jsonObject.getString("dateTime"))
                if (jsonObject.getString("fileURL").equals(""))
                {
                    itemView.img.visibility = View.GONE
                    itemView.t3.visibility = View.GONE
                }
                else {
                    val exteFile=jsonObject.getString("fileURL").substring(jsonObject.getString("fileURL").lastIndexOf("."))
                    if(exteFile.equals("png",true) ||
                        exteFile.equals("jpg",true) ||
                        exteFile.equals("jpeg",true)){
                        itemView.img.visibility = View.VISIBLE
                        itemView.t3.visibility = View.GONE
                        Glide.with(context).load(jsonObject.getString("fileURL")).into(itemView.img);
                        itemView.img.setOnClickListener { context.showImagePopup(jsonObject.getString("photoURL")) }
                    }
                    else
                    {
                        itemView.t3.visibility = View.VISIBLE
                        itemView.img.visibility = View.GONE
                        itemView.t3.setText("Click to View Attachment")
                        itemView.t3.setOnClickListener { context.redirectToWeb2(jsonObject.getString("attachmentURL")) }
                        itemView.t3.paintFlags = itemView.t3.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG

                    }
                }
            }

        }
    }
}
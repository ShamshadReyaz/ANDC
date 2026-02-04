package com.mobiquel.dyalsinghapp.view.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobiquel.dyalsinghapp.utils.redirectToWeb2
import org.json.JSONObject
import com.mobiquel.dyalsinghapp.databinding.ListItemNoticesBinding
import com.mobiquel.dyalsinghapp.utils.showImagePopup


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class NoticeListAdapter(
    var context: Context,
    private var levelList: List<String>
) : RecyclerView.Adapter<NoticeListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemNoticesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return levelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hobby = levelList[position]
        holder.setData(hobby, position)
    }

    inner class ViewHolder(val binding: ListItemNoticesBinding) : RecyclerView.ViewHolder(binding.root) {

        var currentRankModel: String? = null
        var currentPosition: Int = 0
        fun setData(rankModel: String?, pos: Int) {
            rankModel?.let {
                try{
                    val jsonObject = JSONObject(levelList.get(pos))
                    binding.t1.setText(jsonObject.getString("title"))
                    binding.t2.setText(jsonObject.getString("description"))
                    binding.t4.setText(jsonObject.getString("postedBy"))
                    binding.t5.setText(jsonObject.getString("createdOn"))

                    if (jsonObject.getString("photoURL").equals(""))
                        binding.img.visibility = View.GONE
                    else {
                        binding.img.visibility = View.VISIBLE
                        Glide.with(context).load(jsonObject.getString("photoURL")).into(binding.img);

                    }
                    binding.img.setOnClickListener {
                        context.showImagePopup(jsonObject.getString("photoURL"))
                    }
                    if (jsonObject.getString("attachmentURL").equals(""))
                        binding.t3.visibility = View.GONE
                    else {
                        binding.t3.visibility = View.VISIBLE
                        binding.t3.setText("Click to View Attachment")
                        binding.t3.setOnClickListener {
                            context.redirectToWeb2(jsonObject.getString("attachmentURL"))
                        }
                        binding.t3.paintFlags =
                            binding.t3.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG

                    }


                }catch (e:Exception){}

            }

            this.currentRankModel = rankModel
            this.currentPosition = pos
        }
    }
}
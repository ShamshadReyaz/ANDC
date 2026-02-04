package com.mobiquel.dyalsinghapp.view.adapter

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobiquel.dyalsinghapp.utils.redirectToWeb2
import org.json.JSONObject
import com.mobiquel.dyalsinghapp.databinding.ListItemAssignmentsBinding
import com.mobiquel.dyalsinghapp.utils.showImagePopup
import org.json.JSONArray


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class AssignmentsMessagesListAdapter(
    var context: Context,
    private var levelList: JSONArray
) : RecyclerView.Adapter<AssignmentsMessagesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemAssignmentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return levelList.length()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hobby = levelList.getJSONObject(position)
        holder.setData(hobby, position)
    }

    inner class ViewHolder(val binding: ListItemAssignmentsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(rankModel: JSONObject?, pos: Int) {
            rankModel?.let {
                val jsonObject = rankModel
                binding.t1.text = jsonObject.getString("facultyName")
                binding.t2.text = jsonObject.getString("message")
                binding.t4.text = jsonObject.getString("dateTime")
                if (jsonObject.getString("fileURL").equals(""))
                {
                    binding.img.visibility = View.GONE
                    binding.t3.visibility = View.GONE
                }
                else {
                    val exteFile=jsonObject.getString("fileURL").substring(jsonObject.getString("fileURL").lastIndexOf("."))
                    Log.e("exteFile",exteFile)
                    if(exteFile.equals("png",true) ||
                        exteFile.equals("jpg",true) ||
                        exteFile.equals("jpeg",true)){
                        binding.img.visibility = View.VISIBLE
                        binding.t3.visibility = View.GONE
                        Glide.with(context).load(jsonObject.getString("fileURL")).into(binding.img);
                        binding.img.setOnClickListener { context.showImagePopup(jsonObject.getString("fileURL")) }
                    }
                    else
                    {
                        binding.t3.visibility = View.VISIBLE
                        binding.img.visibility = View.GONE
                        binding.t3.setText("Click to View Attachment")
                        binding.t3.setOnClickListener { context.redirectToWeb2(jsonObject.getString("fileURL")) }
                        binding.t3.paintFlags = binding.t3.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG

                    }
                }
            }

        }
    }
}
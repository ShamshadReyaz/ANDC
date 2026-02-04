package com.mobiquel.dyalsinghapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import com.mobiquel.dyalsinghapp.databinding.ListItemMaintananceBinding


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class MaintanceListAdapter(
    var context: Context,
    private var levelList: List<String>
) : RecyclerView.Adapter<MaintanceListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ListItemMaintananceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return levelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hobby = levelList[position]
        holder.setData(hobby, position)
    }

    inner class ViewHolder(val binding: ListItemMaintananceBinding) : RecyclerView.ViewHolder(binding.root) {

        var currentRankModel: String? = null
        var currentPosition: Int = 0
        fun setData(rankModel: String?, pos: Int) {
            rankModel?.let {
                try {
                    val jsonObject = JSONObject(levelList.get(pos))
                    binding.reqId.setText("#"+jsonObject.getString("requisitionNo"))
                    binding.category.setText("Category: "+jsonObject.getString("requsitionCategory"))
                    binding.location.setText("Location: "+jsonObject.getString("location"))
                    binding.description.setText("Description:\n"+jsonObject.getString("description"))
                    binding.createdOn.setText(jsonObject.getString("requisitionDateTime"))

                    if(jsonObject.getString("isSOSMarked").equals("T"))
                        binding.sos.visibility=View.VISIBLE
                    else
                        binding.sos.visibility=View.GONE

                }catch (e:Exception){

                }

            }

            this.currentRankModel = rankModel
            this.currentPosition = pos
        }
    }
}
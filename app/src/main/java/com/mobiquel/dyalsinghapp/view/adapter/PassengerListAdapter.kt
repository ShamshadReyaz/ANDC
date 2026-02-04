package com.mobiquel.dyalsinghapp.view.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import com.mobiquel.dyalsinghapp.databinding.ListItemPassengerBinding


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class PassengerListAdapter(
    var context: Context,
    var vehicleType:String,
    private var levelList: List<String>
) : RecyclerView.Adapter<PassengerListAdapter.ViewHolder>() {

    var checkedPassenId = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val view = LayoutInflater.from(context).inflate(R.layout.list_item_passenger, parent, false)
        val binding = ListItemPassengerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return levelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hobby = levelList[position]
        holder.setData(hobby, position)
    }

    inner class ViewHolder(val binding: ListItemPassengerBinding) : RecyclerView.ViewHolder(binding.root) {

        var currentRankModel: String? = null
        var currentPosition: Int = 0
        fun setData(rankModel: String?, pos: Int) {
            rankModel?.let {
                var j = pos + 1
                var jsonObject = JSONObject(levelList.get(pos))
                if(vehicleType.equals("Helicopter")){
                    binding.messageId.setText("#"+j+" "+  jsonObject.getString("passengerName"))
                    binding.message.setText("Gender: " + jsonObject.getString("gender"))
                    val jsonObject2=JSONObject(jsonObject.getString("idProof"))
                    binding.contactNumber.setText("ID Number: " + jsonObject2.getString("no"))
                    binding.idType.setText("ID Type: " + jsonObject2.getString("type"))
                    binding.createdOn.visibility=View.VISIBLE
                    binding.createdOn.setText("CLick to view document")
                    binding.createdOn.setOnClickListener {
                        val openURL = Intent(Intent.ACTION_VIEW)
                        openURL.data = Uri.parse(jsonObject2.getString("document"))
                        context.startActivity(openURL)
                    }


                }
                else{
                    binding.messageId.setText("#"+j+" "+  jsonObject.getString("passengerName"))
                    binding.message.setText("Gender: " + jsonObject.getString("gender"))
                    binding.contactNumber.setText("ID Number: " + jsonObject.getString("idNo"))
                    binding.idType.setText("ID Type: " + jsonObject.getString("idType"))
                    binding.createdOn.visibility=View.GONE

                }



                binding.checkin.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked)
                        checkedPassenId =
                            checkedPassenId + jsonObject.getString("passengerId") + ","
                    else
                        checkedPassenId=checkedPassenId.replace(jsonObject.getString("passengerId") + ",", "")
                }

                if(jsonObject.getString("isCheckedIn").equals("T"))
                {
                    binding.checkin.isChecked=true
                    binding.checkin.isEnabled=false
                }
                else{
                    binding.checkin.isChecked=false
                    binding.checkin.isEnabled=true

                }

            }

            this.currentRankModel = rankModel
            this.currentPosition = pos
        }
    }

    public fun getCheckedIds(): String {
        return checkedPassenId
    }
}
package com.mobiquel.srccapp.view.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_passenger.view.*
import org.json.JSONObject
import com.mobiquel.srccapp.R


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
                var j = pos + 1
                var jsonObject = JSONObject(levelList.get(pos))
                if(vehicleType.equals("Helicopter")){
                    itemView.messageId.setText("#"+j+" "+  jsonObject.getString("passengerName"))
                    itemView.message.setText("Gender: " + jsonObject.getString("gender"))
                    val jsonObject2=JSONObject(jsonObject.getString("idProof"))
                    itemView.contactNumber.setText("ID Number: " + jsonObject2.getString("no"))
                    itemView.idType.setText("ID Type: " + jsonObject2.getString("type"))
                    itemView.createdOn.visibility=View.VISIBLE
                    itemView.createdOn.setText("CLick to view document")
                    itemView.createdOn.setOnClickListener {
                        val openURL = Intent(Intent.ACTION_VIEW)
                        openURL.data = Uri.parse(jsonObject2.getString("document"))
                        context.startActivity(openURL)
                    }


                }
                else{
                    itemView.messageId.setText("#"+j+" "+  jsonObject.getString("passengerName"))
                    itemView.message.setText("Gender: " + jsonObject.getString("gender"))
                    itemView.contactNumber.setText("ID Number: " + jsonObject.getString("idNo"))
                    itemView.idType.setText("ID Type: " + jsonObject.getString("idType"))
                    itemView.createdOn.visibility=View.GONE

                }



                itemView.checkin.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked)
                        checkedPassenId =
                            checkedPassenId + jsonObject.getString("passengerId") + ","
                    else
                        checkedPassenId=checkedPassenId.replace(jsonObject.getString("passengerId") + ",", "")
                }

                if(jsonObject.getString("isCheckedIn").equals("T"))
                {
                    itemView.checkin.isChecked=true
                    itemView.checkin.isEnabled=false
                }
                else{
                    itemView.checkin.isChecked=false
                    itemView.checkin.isEnabled=true

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
package com.mobiquel.dyalsinghapp.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.mobiquel.dyalsinghapp.databinding.ListItemAttendanceDetailBinding

import org.json.JSONArray
import org.json.JSONObject


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class ListOfStudentAttendanceDetailAdapter(
    var context: Context,
    private var listOfAttendance: JSONArray,
    private var typeOfClass: String
) : RecyclerView.Adapter<ListOfStudentAttendanceDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemAttendanceDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfAttendance.length()
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val hobby = listOfAttendance.getJSONObject(position)

        holder.setData(hobby, position)
    }

    inner class ViewHolder(val binding: ListItemAttendanceDetailBinding) : RecyclerView.ViewHolder(binding.root) {


        fun setData(rankModel: JSONObject?, pos: Int) {
            binding.month.setText(rankModel?.getString("facultyName"))
            //itemView.faculty.setText("Benefits: "+(if(rankModel?.isNull("benefits") == true) "-" else rankModel?.getString("benefits")))
            val benefits = rankModel?.optString("benefits")?.takeIf { it != "null" } ?: "0"
            binding.faculty.setText("Benefits: "+benefits)
            binding.classHeld.setText("Lecture: "+(if(rankModel?.isNull("lectureAttended") == true) "-" else rankModel?.getString("lectureAttended"))+" / "+(if(rankModel?.isNull("lectureHeld") == true) "-" else rankModel?.getString("lectureHeld")))
            binding.benefits.setText("Lab: "+(if(rankModel?.isNull("labAttended") == true) "-" else rankModel?.getString("labAttended"))+" / "+(if(rankModel?.isNull("labHeld") == true) "-" else rankModel?.getString("labHeld")))
            binding.classAttended.setText("Tutorial: "+(if(rankModel?.isNull("tutorialAttended") == true) "-" else rankModel?.getString("tutorialAttended"))+" / "+(if(rankModel?.isNull("tutorialHeld") == true) "-" else rankModel?.getString("tutorialHeld")))


        }
    }
}
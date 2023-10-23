package com.mobiquel.srccapp.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener2
import kotlinx.android.synthetic.main.list_item_slot_attendance_student.view.*
import com.mobiquel.srccapp.R
import com.mobiquel.srccapp.pojo.AttendanceStudentModel
import com.mobiquel.srccapp.pojo.OfflineAttendanceModel
import com.mobiquel.srccapp.pojo.SlotAttendanceStudentModel
import com.mobiquel.srccapp.room.entity.AttendanceClassEntity
import kotlinx.android.synthetic.main.dialog_show_attendance_detail.view.*
import kotlinx.android.synthetic.main.list_item_attendance_detail.view.*
import kotlinx.android.synthetic.main.list_item_offline_attendance.view.*
import kotlinx.android.synthetic.main.list_item_offline_attendance.view.t1
import kotlinx.android.synthetic.main.list_item_offline_attendance.view.t2
import kotlinx.android.synthetic.main.list_item_student_attendance.view.*
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
        val view = LayoutInflater.from(context)
            .inflate(R.layout.list_item_attendance_detail, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfAttendance.length()
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val hobby = listOfAttendance.getJSONObject(position)

        holder.setData(hobby, position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun setData(rankModel: JSONObject?, pos: Int) {
            itemView.month.setText(rankModel?.getString("month"))
            itemView.type.setText("Type: "+typeOfClass)
            Log.e("CREDITS",""+rankModel?.getJSONObject("paperDetails")?.getString("creditsLecture"))
            itemView.faculty.setText("By "+rankModel?.getString("facultyName"))
            itemView.classHeld.setText("Classes Held: "+rankModel?.getString("classesHeld"))
            itemView.classAttended.setText("Classes Attended: "+rankModel?.getString("classesAttended"))
            itemView.benefits.setText("Benefits: "+rankModel?.getString("benefits"))


        }
    }
}
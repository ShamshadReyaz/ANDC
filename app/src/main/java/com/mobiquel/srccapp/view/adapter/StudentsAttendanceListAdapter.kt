package com.mobiquel.srccapp.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_attendance_student.view.*
import com.mobiquel.srccapp.R
import com.mobiquel.srccapp.pojo.AttendanceStudentModel
import com.mobiquel.srccapp.view.fragment.AttendanceFragment


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class StudentsAttendanceListAdapter(
    var context: Context,
    private var attendanceFragment: AttendanceFragment
) : RecyclerView.Adapter<StudentsAttendanceListAdapter.ViewHolder>() {
    var listOfAttendance=ArrayList<AttendanceStudentModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.list_item_attendance_student, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfAttendance.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hobby = listOfAttendance[position]
        holder.setData(hobby, position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun setData(rankModel: AttendanceStudentModel?, pos: Int) {
            rankModel?.let {

                val sno = pos + 1
                itemView.sno.text = "" + sno
                itemView.rollnumber.text = "" + it.rollNo
                itemView.name.text = it.studentName
                itemView.status.isChecked = !it.isPresent.equals("A")
                if (pos % 2 == 0)
                    itemView.rl_main.setBackgroundColor(Color.parseColor("#F4F3F3"))
                else
                    itemView.rl_main.setBackgroundColor(Color.parseColor("#FFFFFF"))
                itemView.status.setOnClickListener {
                    if (listOfAttendance.get(pos).isPresent.equals("A"))
                        listOfAttendance.get(pos).isPresent="P"
                    else
                        listOfAttendance.get(pos).isPresent="A"
                    attendanceFragment.updateBottoomSheet()
                    notifyDataSetChanged()
                }

            }

        }
    }

    fun updateList(list:List<AttendanceStudentModel>){
        listOfAttendance=ArrayList()
        listOfAttendance.addAll(list)
        notifyDataSetChanged()
    }
}
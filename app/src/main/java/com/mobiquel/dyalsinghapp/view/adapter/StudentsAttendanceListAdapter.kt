package com.mobiquel.dyalsinghapp.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobiquel.dyalsinghapp.databinding.ListItemAttendanceStudentBinding
import com.mobiquel.dyalsinghapp.pojo.AttendanceStudentModel
import com.mobiquel.dyalsinghapp.view.fragment.AttendanceFragment


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class StudentsAttendanceListAdapter(
    var context: Context,
    private var attendanceFragment: AttendanceFragment
) : RecyclerView.Adapter<StudentsAttendanceListAdapter.ViewHolder>() {
    var listOfAttendance=ArrayList<AttendanceStudentModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ListItemAttendanceStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfAttendance.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hobby = listOfAttendance[position]
        holder.setData(hobby, position)
    }

    inner class ViewHolder(val binding: ListItemAttendanceStudentBinding) : RecyclerView.ViewHolder(binding.root) {


        fun setData(rankModel: AttendanceStudentModel?, pos: Int) {
            rankModel?.let {

                val sno = pos + 1
                binding.sno.text = "" + sno
                binding.rollnumber.text = "" + it.rollNo
                binding.name.text = it.studentName
                binding.status.isChecked = !it.isPresent.equals("A")
                if (pos % 2 == 0)
                    binding.rlMain.setBackgroundColor(Color.parseColor("#F4F3F3"))
                else
                    binding.rlMain.setBackgroundColor(Color.parseColor("#FFFFFF"))
                binding.status.setOnClickListener {
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
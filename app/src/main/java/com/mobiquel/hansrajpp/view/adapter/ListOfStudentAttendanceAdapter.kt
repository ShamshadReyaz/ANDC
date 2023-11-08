package com.mobiquel.hansrajpp.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener
import kotlinx.android.synthetic.main.list_item_slot_attendance_student.view.*
import com.mobiquel.hansrajapp.R
import kotlinx.android.synthetic.main.list_item_offline_attendance.view.*
import kotlinx.android.synthetic.main.list_item_offline_attendance.view.t1
import kotlinx.android.synthetic.main.list_item_offline_attendance.view.t2
import kotlinx.android.synthetic.main.list_item_student_attendance.view.*


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class ListOfStudentAttendanceAdapter(
    var context: Context,
    private var listOfAttendance: List<String>,
    private var recyclerItemClickListener: RecyclerItemClickListener
) : RecyclerView.Adapter<ListOfStudentAttendanceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.list_item_student_attendance, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfAttendance.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val hobby = listOfAttendance[position]

        holder.setData(hobby, position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun setData(rankModel: String?, pos: Int) {
            rankModel?.let {
                val data=rankModel.split("separator")
                itemView.t1.text =data[0]
                itemView.t2.text =data[1]

            }
            itemView.viewlayout.setOnClickListener {
                recyclerItemClickListener.onRecyclerItemClicked(pos)
            }

        }
    }
}
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
import com.mobiquel.hansrajpp.room.entity.AttendanceClassEntity

import kotlinx.android.synthetic.main.list_item_offline_attendance.view.*


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class ListOfOfflineAttendanceAdapter(
    var context: Context,
    private var listOfAttendance: List<AttendanceClassEntity>,
    private var recyclerItemClickListener: RecyclerItemClickListener
) : RecyclerView.Adapter<ListOfOfflineAttendanceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.list_item_offline_attendance, parent, false)
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


        fun setData(rankModel: AttendanceClassEntity?, pos: Int) {
            rankModel?.let {

                itemView.t1.text =rankModel.virtualGroupName+" ("+rankModel.paperName+")"
                itemView.t2.text =rankModel.sessionDate

            }
            itemView.view.setOnClickListener {

                recyclerItemClickListener.onRecyclerItemClicked(pos)

            }
           /* itemView.sync.setOnClickListener {


            }*/

        }
    }
}
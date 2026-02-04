package com.mobiquel.dyalsinghapp.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener
import com.mobiquel.dyalsinghapp.databinding.ListItemOfflineAttendanceBinding
import com.mobiquel.dyalsinghapp.room.entity.AttendanceClassEntity



/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class ListOfOfflineAttendanceAdapter(
    var context: Context,
    private var listOfAttendance: List<AttendanceClassEntity>,
    private var recyclerItemClickListener: RecyclerItemClickListener
) : RecyclerView.Adapter<ListOfOfflineAttendanceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemOfflineAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfAttendance.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val hobby = listOfAttendance[position]

        holder.setData(hobby, position)
    }

    inner class ViewHolder(val binding: ListItemOfflineAttendanceBinding) : RecyclerView.ViewHolder(binding.root) {


        fun setData(rankModel: AttendanceClassEntity?, pos: Int) {
            rankModel?.let {

                binding.t1.text =rankModel.virtualGroupName+" ("+rankModel.paperName+")"
                binding.t2.text =rankModel.sessionDate

            }
            binding.view.setOnClickListener {

                recyclerItemClickListener.onRecyclerItemClicked(pos)

            }
           /* itemView.sync.setOnClickListener {


            }*/

        }
    }
}
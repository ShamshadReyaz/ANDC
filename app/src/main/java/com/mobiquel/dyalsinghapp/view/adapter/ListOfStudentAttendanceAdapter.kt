package com.mobiquel.dyalsinghapp.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener

import com.mobiquel.dyalsinghapp.databinding.ListItemStudentAttendanceBinding


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class ListOfStudentAttendanceAdapter(
    var context: Context,
    private var listOfAttendance: List<String>,
    private var recyclerItemClickListener: RecyclerItemClickListener
) : RecyclerView.Adapter<ListOfStudentAttendanceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val binding = ListItemStudentAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfAttendance.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val hobby = listOfAttendance[position]

        holder.setData(hobby, position)
    }

    inner class ViewHolder(val binding: ListItemStudentAttendanceBinding) : RecyclerView.ViewHolder(binding.root) {


        fun setData(rankModel: String?, pos: Int) {
            rankModel?.let {
                val data=rankModel.split("separator")
                binding.t1.text =data[0]

                //itemView.t2.text =data[1]
                //val typeOfClass =it..getJSONObject(0).getString("groupType")
                //itemView.t3.text ="Group Type: "+data[2]

            }

            binding.viewlayout.setOnClickListener {
                recyclerItemClickListener.onRecyclerItemClicked(pos)
            }

        }
    }
}
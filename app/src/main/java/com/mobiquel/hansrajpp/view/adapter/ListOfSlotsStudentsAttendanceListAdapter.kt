package com.mobiquel.hansrajpp.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener2
import kotlinx.android.synthetic.main.list_item_slot_attendance_student.view.*
import com.mobiquel.hansrajapp.R
import com.mobiquel.hansrajpp.pojo.SlotAttendanceStudentModel


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class ListOfSlotsStudentsAttendanceListAdapter(
    var context: Context,
    private var recyclerItemClickListener: RecyclerItemClickListener2,
    private var recyclerItemClickListener2: RecyclerItemClickListener
) : RecyclerView.Adapter<ListOfSlotsStudentsAttendanceListAdapter.ViewHolder>() {
    var listOfAttendance=ArrayList<SlotAttendanceStudentModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.list_item_slot_attendance_student, parent, false)
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


        fun setData(rankModel: SlotAttendanceStudentModel?, pos: Int) {
            rankModel?.let {

                itemView.slotName.text = "Slot " + pos.plus(1)
                itemView.presentAbsent.text = "Present: " + it.present + " Absent: " + it.absent

                if (it.isSelected.equals("T")) {
                    itemView.rl_main.setBackgroundResource(R.drawable.rectangle_background_slot_attenadnce_3)
                    itemView.slotName.setTextColor(Color.parseColor("#000000"))
                    itemView.presentAbsent.setTextColor(Color.parseColor("#000000"))
                } else {
                    itemView.rl_main.setBackgroundResource(R.drawable.rectangle_background_slot_attenadnce)
                    itemView.slotName.setTextColor(Color.parseColor("#000000"))
                    itemView.presentAbsent.setTextColor(Color.parseColor("#000000"))
                }

            }
            itemView.rl_main.setOnClickListener {
                /*val previousPos=listOfAttendance.filterIndexed { index, slotAttendanceStudentModel ->
                    slotAttendanceStudentModel.isSelected.equals("P")
                }*/
                var  previousPos=0
                for(model in listOfAttendance){
                    if(model.isSelected.equals("T"))
                        break
                    else
                        previousPos++
                }
                recyclerItemClickListener.onRecyclerItemClicked(pos, previousPos)

            }
            itemView.deleteSlot.setOnClickListener {
                recyclerItemClickListener2.onRecyclerItemClicked(pos)
            }

        }
    }
    fun updateList(list:List<SlotAttendanceStudentModel>){
        listOfAttendance=ArrayList()
        listOfAttendance.addAll(list)
        notifyDataSetChanged()
    }
}
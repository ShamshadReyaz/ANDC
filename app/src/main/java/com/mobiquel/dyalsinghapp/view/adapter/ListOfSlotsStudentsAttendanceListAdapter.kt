package com.mobiquel.dyalsinghapp.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener2
import com.mobiquel.dyalsinghapp.R
import com.mobiquel.dyalsinghapp.databinding.ListItemSlotAttendanceStudentBinding
import com.mobiquel.dyalsinghapp.pojo.SlotAttendanceStudentModel


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
        val binding = ListItemSlotAttendanceStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfAttendance.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val hobby = listOfAttendance[position]

        holder.setData(hobby, position)
    }

    inner class ViewHolder(val binding: ListItemSlotAttendanceStudentBinding) : RecyclerView.ViewHolder(binding.root) {


        fun setData(rankModel: SlotAttendanceStudentModel?, pos: Int) {
            rankModel?.let {

                binding.slotName.text = "Slot " + pos.plus(1)
                binding.presentAbsent.text = "Present: " + it.present + " Absent: " + it.absent

                if (it.isSelected.equals("T")) {
                    binding.rlMain.setBackgroundResource(R.drawable.rectangle_background_slot_attenadnce_3)
                    binding.slotName.setTextColor(Color.parseColor("#000000"))
                    binding.presentAbsent.setTextColor(Color.parseColor("#000000"))
                } else {
                    binding.rlMain.setBackgroundResource(R.drawable.rectangle_background_slot_attenadnce)
                    binding.slotName.setTextColor(Color.parseColor("#000000"))
                    binding.presentAbsent.setTextColor(Color.parseColor("#000000"))
                }

            }
            binding.rlMain.setOnClickListener {
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
            binding.deleteSlot.setOnClickListener {
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
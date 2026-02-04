package com.mobiquel.dyalsinghapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobiquel.dyalsinghapp.databinding.ListItemButtonsBinding
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener
import com.mobiquel.dyalsinghapp.pojo.ButtonModel

/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class ButtonListAdapter(
    var context: Context,
    private var levelList: List<ButtonModel>,
    private var onRecyclerItemClickListener: RecyclerItemClickListener
) : RecyclerView.Adapter<ButtonListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemButtonsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return levelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hobby = levelList[position]
        holder.setData(hobby, position)
    }

    inner class ViewHolder(val binding: ListItemButtonsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(rankModel: ButtonModel?, pos: Int) {
            val img = context.getResources().getDrawable(rankModel!!.drawable!!)
            binding.title.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null)
            binding.title.setText(rankModel!!.name)
            binding.rlMain.setOnClickListener {
                onRecyclerItemClickListener.onRecyclerItemClicked(pos)
            }
        }
    }
}
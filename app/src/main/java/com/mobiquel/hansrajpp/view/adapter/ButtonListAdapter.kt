package com.mobiquel.hansrajpp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobiquel.hansrajapp.R
import com.mobiquel.mdt112.*
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener
import com.mobiquel.hansrajpp.pojo.ButtonModel
import kotlinx.android.synthetic.main.list_item_buttons.view.*
import kotlinx.android.synthetic.main.list_item_notices.view.*


/**
 * Created by Arman Reyaz on 12/22/2020.
 */
class ButtonListAdapter(
    var context: Context,
    private var levelList: List<ButtonModel>,
    private var onRecyclerItemClickListener: RecyclerItemClickListener
) : RecyclerView.Adapter<ButtonListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_buttons, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return levelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hobby = levelList[position]
        holder.setData(hobby, position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(rankModel: ButtonModel?, pos: Int) {
            val img = context.getResources().getDrawable(rankModel!!.drawable!!)
            itemView.title.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null)
            itemView.title.setText(rankModel!!.name)
            itemView.setOnClickListener {
                onRecyclerItemClickListener.onRecyclerItemClicked(pos)
            }
        }
    }
}
package com.ferroli.after_sales.agentOrder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ferroli.after_sales.R
import com.ferroli.after_sales.entity.AgentOrderLine
import com.ferroli.after_sales.entity.urlFileBase
import com.google.android.material.textfield.TextInputEditText

class AgentOrderCellAdapter :
    ListAdapter<AgentOrderLine, AgentOrderCellAdapter.CellViewHolder>(DIFFCALLBACK) {
    object DIFFCALLBACK : DiffUtil.ItemCallback<AgentOrderLine>() {
        override fun areItemsTheSame(
            oldItem: AgentOrderLine,
            newItem: AgentOrderLine
        ): Boolean {
            return oldItem.aOlId == newItem.aOlId
        }

        override fun areContentsTheSame(
            oldItem: AgentOrderLine,
            newItem: AgentOrderLine
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class CellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgDefaultAgentOrderRecord: ImageView =
            itemView.findViewById(R.id.imgDefaultAgentOrderRecord)
        val tvBPaiNameAgentOrderRecord: TextView =
            itemView.findViewById(R.id.tvBPaiNameAgentOrderRecord)
        val tvBPaiCodeAgentOrderRecord: TextView =
            itemView.findViewById(R.id.tvBPaiCodeAgentOrderRecord)
        val tvBPaiAgentPriceAgentOrderRecord: TextView =
            itemView.findViewById(R.id.tvBPaiAgentPriceAgentOrderRecord)
        val tbAOlCountAgentOrderRecord: TextInputEditText =
            itemView.findViewById(R.id.til1AgentOrderRecord)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_agent_order_record, parent, false)
            .apply {
                return CellViewHolder(this)
            }
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val currentItem = getItem(position)

//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        holder.tvBPaiNameAgentOrderRecord.text = currentItem.bPaiName
        holder.tvBPaiCodeAgentOrderRecord.text = currentItem.bPaiCode
        holder.tvBPaiAgentPriceAgentOrderRecord.text = currentItem.aOlAgentPrice.toString()
        holder.tbAOlCountAgentOrderRecord.setText(currentItem.aOlCount.toString())

        Glide.with(holder.itemView)
            .load(urlFileBase + currentItem.bPaiCode + ".jpg")
            .centerCrop()
            .placeholder(R.drawable.ic_default_part_img)
            .into(holder.imgDefaultAgentOrderRecord)
    }
}
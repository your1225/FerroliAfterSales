package com.ferroli.after_sales.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ferroli.after_sales.R
import com.ferroli.after_sales.entity.DetailInfo

class DetailCellAdapter : ListAdapter<DetailInfo, DetailCellAdapter.CellViewHolder>(
    DIFFCALLBACK
) {
    object DIFFCALLBACK : DiffUtil.ItemCallback<DetailInfo>() {
        override fun areItemsTheSame(
            oldItem: DetailInfo,
            newItem: DetailInfo
        ): Boolean {
            return oldItem.titleString == newItem.titleString
        }

        override fun areContentsTheSame(
            oldItem: DetailInfo,
            newItem: DetailInfo
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class CellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitleCellDetailRecord: TextView =
            itemView.findViewById(R.id.tvTitleCellDetailRecord)
        val tvContentCellDetailRecord: TextView =
            itemView.findViewById(R.id.tvContentCellDetailRecord)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_detail_record, parent, false)
            .apply {
                return CellViewHolder(this)
            }
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val currentItem = getItem(position)

//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        holder.tvTitleCellDetailRecord.text = currentItem.titleString
        holder.tvContentCellDetailRecord.text = currentItem.contentString
    }
}
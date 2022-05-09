package com.ferroli.after_sales.basePartInfo

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
import com.ferroli.after_sales.entity.BasePartInfo
import com.ferroli.after_sales.entity.urlFileBase

class BasePartInfoCellAdapter : ListAdapter<BasePartInfo, BasePartInfoCellAdapter.CellViewHolder>(
    DIFFCALLBACK
) {
    object DIFFCALLBACK : DiffUtil.ItemCallback<BasePartInfo>() {
        override fun areItemsTheSame(
            oldItem: BasePartInfo,
            newItem: BasePartInfo
        ): Boolean {
            return oldItem.bPaiCode == newItem.bPaiCode
        }

        override fun areContentsTheSame(
            oldItem: BasePartInfo,
            newItem: BasePartInfo
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class CellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgDefaultBasePartInfoRecord: ImageView =
            itemView.findViewById(R.id.imgDefaultBasePartInfoRecord)
        val tvBPcNameBasePartInfoRecord: TextView =
            itemView.findViewById(R.id.tvBPcNameBasePartInfoRecord)
        val tvBPaiNameBasePartInfoRecord: TextView =
            itemView.findViewById(R.id.tvBPaiNameBasePartInfoRecord)
        val tvBPaiCodeBasePartInfoRecord: TextView =
            itemView.findViewById(R.id.tvBPaiCodeBasePartInfoRecord)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_base_part_info_record, parent, false)
            .apply {
                return CellViewHolder(this)
            }
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val currentItem = getItem(position)

//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        holder.tvBPaiNameBasePartInfoRecord.text = currentItem.bPaiName
        holder.tvBPaiCodeBasePartInfoRecord.text = currentItem.bPaiCode
        holder.tvBPcNameBasePartInfoRecord.text = currentItem.bPcName

        Glide.with(holder.itemView)
            .load(urlFileBase + currentItem.bPaiCode + ".jpg")
            .centerCrop()
            .placeholder(R.drawable.ic_default_part_img)
            .into(holder.imgDefaultBasePartInfoRecord)
    }
}
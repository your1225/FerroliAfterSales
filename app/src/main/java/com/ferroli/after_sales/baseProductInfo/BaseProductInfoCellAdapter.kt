package com.ferroli.after_sales.baseProductInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ferroli.after_sales.R
import com.ferroli.after_sales.entity.BaseProductInfo

class BaseProductInfoCellAdapter(private val listener: OnItemCheckedListener) :
    ListAdapter<BaseProductInfo, BaseProductInfoCellAdapter.CellViewHolder>(
        DIFFCALLBACK
    ) {
    object DIFFCALLBACK : DiffUtil.ItemCallback<BaseProductInfo>() {
        override fun areItemsTheSame(
            oldItem: BaseProductInfo,
            newItem: BaseProductInfo
        ): Boolean {
            return oldItem.bPiCode == newItem.bPiCode
        }

        override fun areContentsTheSame(
            oldItem: BaseProductInfo,
            newItem: BaseProductInfo
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class CellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBPtNameBaseProductInfoRecord: TextView =
            itemView.findViewById(R.id.tvBPtNameBaseProductInfoRecord)
        val tvBPmNameBaseProductInfoRecord: TextView =
            itemView.findViewById(R.id.tvBPmNameBaseProductInfoRecord)
        val tvBPiNameBaseProductInfoRecord: TextView =
            itemView.findViewById(R.id.tvBPiNameBaseProductInfoRecord)
        val tvBPiENameBaseProductInfoRecord: TextView =
            itemView.findViewById(R.id.tvBPiENameBaseProductInfoRecord)
        val layoutMainBaseProductInfoRecord: ConstraintLayout =
            itemView.findViewById(R.id.layoutMainBaseProductInfoRecord)
    }

    interface OnItemCheckedListener {
        fun onItemChecked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_base_product_info_record, parent, false)
            .apply {
                return CellViewHolder(this)
            }
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val currentItem = getItem(position)

//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        holder.tvBPtNameBaseProductInfoRecord.text = currentItem.bPtName
        holder.tvBPmNameBaseProductInfoRecord.text = currentItem.bPmName
        holder.tvBPiNameBaseProductInfoRecord.text = currentItem.bPiName
        holder.tvBPiENameBaseProductInfoRecord.text = currentItem.bPiEName

        holder.layoutMainBaseProductInfoRecord.setOnClickListener {
            listener.onItemChecked(position)
        }
    }
}
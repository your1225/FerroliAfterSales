package com.ferroli.after_sales.basePartInfo

import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ferroli.after_sales.R
import com.ferroli.after_sales.entity.BasePartInfo
import com.ferroli.after_sales.entity.urlFileBase
import com.google.android.material.textfield.TextInputEditText

class BasePartInfoCellAdapter(private val listener: OnItemCheckedListener) :
    ListAdapter<BasePartInfo, BasePartInfoCellAdapter.CellViewHolder>(
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
        val tvBPaiNameBasePartInfoRecord: TextView =
            itemView.findViewById(R.id.tvBPaiNameBasePartInfoRecord)
        val tvBPaiCodeBasePartInfoRecord: TextView =
            itemView.findViewById(R.id.tvBPaiCodeBasePartInfoRecord)
        val layoutBasePartInfoRecord: ConstraintLayout =
            itemView.findViewById(R.id.layoutBasePartInfoRecord)
    }

    interface OnItemCheckedListener {
        fun onItemChecked(position: Int)
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

        holder.layoutBasePartInfoRecord.setOnClickListener {
            listener.onItemChecked(position)
        }
    }
}
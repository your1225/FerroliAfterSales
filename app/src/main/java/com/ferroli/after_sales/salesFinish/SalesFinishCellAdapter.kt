package com.ferroli.after_sales.salesFinish

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ferroli.after_sales.R
import com.ferroli.after_sales.entity.SalesFinishLine
import com.ferroli.after_sales.entity.urlFileBase

class SalesFinishCellAdapter(private val listener: OnItemOperationListener):
    ListAdapter<SalesFinishLine, SalesFinishCellAdapter.CellViewHolder>(
        DIFFCALLBACK
    ) {
    object DIFFCALLBACK : DiffUtil.ItemCallback<SalesFinishLine>() {
        override fun areItemsTheSame(
            oldItem: SalesFinishLine,
            newItem: SalesFinishLine
        ): Boolean {
            return oldItem.sFlId == newItem.sFlId
        }

        override fun areContentsTheSame(
            oldItem: SalesFinishLine,
            newItem: SalesFinishLine
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemOperationListener {
        fun onDeleteClick(item: SalesFinishLine)
    }

    inner class CellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBPaiNameSalesFinishRecord: TextView =
            itemView.findViewById(R.id.tvBPaiNameSalesFinishRecord)
        val tvBPaiCodeSalesFinishRecord: TextView =
            itemView.findViewById(R.id.tvBPaiCodeSalesFinishRecord)
        val tvCountSalesFinishRecord: TextView =
            itemView.findViewById(R.id.tvCountSalesFinishRecord)
        val imgDefaultSalesFinishRecord: ImageView =
            itemView.findViewById(R.id.imgDefaultSalesFinishRecord)
        val btnDeleteSalesFinishRecord: ImageButton =
            itemView.findViewById(R.id.btnDeleteSalesFinishRecord)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_sales_finish_record, parent, false)
            .apply {
                return CellViewHolder(this)
            }
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val currentItem = getItem(position)

//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        holder.tvBPaiNameSalesFinishRecord.text = currentItem.bPaiName
        holder.tvBPaiCodeSalesFinishRecord.text = currentItem.bPaiCode
        holder.tvCountSalesFinishRecord.text = currentItem.sFlCount.toString()

        Glide.with(holder.itemView)
            .load(urlFileBase + currentItem.bPaiCode + ".jpg")
            .centerCrop()
            .placeholder(R.drawable.ic_default_part_img)
            .into(holder.imgDefaultSalesFinishRecord)

        holder.imgDefaultSalesFinishRecord.setOnClickListener {
            val bundle = bundleOf(
                "PartCode" to currentItem.bPaiCode
            )

            it.findNavController().navigate(
                R.id.action_salesFinishOperationFragment_to_partImageViewFragment,
                bundle
            )
        }

        holder.btnDeleteSalesFinishRecord.setOnClickListener {
            listener.onDeleteClick(currentItem)
        }
    }
}
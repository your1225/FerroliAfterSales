package com.ferroli.after_sales.salesOrder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ferroli.after_sales.R
import com.ferroli.after_sales.entity.SalesOrderLine

class SalesOrderLineCellAdapter: ListAdapter<SalesOrderLine, SalesOrderLineCellAdapter.CellViewHolder>(
    DIFFCALLBACK
) {
    object DIFFCALLBACK : DiffUtil.ItemCallback<SalesOrderLine>() {
        override fun areItemsTheSame(
            oldItem: SalesOrderLine,
            newItem: SalesOrderLine
        ): Boolean {
            return oldItem.sOlId == newItem.sOlId
        }

        override fun areContentsTheSame(
            oldItem: SalesOrderLine,
            newItem: SalesOrderLine
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class CellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBPtNameSalesOrderLineRecord: TextView =
            itemView.findViewById(R.id.tvBPtNameSalesOrderLineRecord)
        val tvBPmNameSalesOrderLineRecord: TextView =
            itemView.findViewById(R.id.tvBPmNameSalesOrderLineRecord)
        val tvSOlNumSalesOrderLineRecord: TextView =
            itemView.findViewById(R.id.tvSOlNumSalesOrderLineRecord)
        val tvBPiNameSalesOrderLineRecord: TextView =
            itemView.findViewById(R.id.tvBPiNameSalesOrderLineRecord)
        val tvBPiENameSalesOrderLineRecord: TextView =
            itemView.findViewById(R.id.tvBPiENameSalesOrderLineRecord)
        val tvSOlMoneySalesOrderLineRecord: TextView =
            itemView.findViewById(R.id.tvSOlMoneySalesOrderLineRecord)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_sales_order_line_record, parent, false)
            .apply {
                return CellViewHolder(this)
            }
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val currentItem = getItem(position)

//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        holder.tvBPtNameSalesOrderLineRecord.text = currentItem.bPtName
        holder.tvBPmNameSalesOrderLineRecord.text = currentItem.bPmName
        holder.tvSOlNumSalesOrderLineRecord.text = currentItem.sOlNum.toString()
        holder.tvBPiNameSalesOrderLineRecord.text = currentItem.bPiName
        holder.tvBPiENameSalesOrderLineRecord.text = currentItem.bPiEName
        holder.tvSOlMoneySalesOrderLineRecord.text = currentItem.sOlMoney.toString()
    }
}
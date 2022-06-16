package com.ferroli.after_sales.salesOrder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ferroli.after_sales.R
import com.ferroli.after_sales.entity.SalesOrder
import java.text.SimpleDateFormat
import java.util.*

class SalesOrderCellAdapter(private val listener: OnItemCheckedListener) :
    ListAdapter<SalesOrder, SalesOrderCellAdapter.CellViewHolder>(
        DIFFCALLBACK
    ) {
    object DIFFCALLBACK : DiffUtil.ItemCallback<SalesOrder>() {
        override fun areItemsTheSame(
            oldItem: SalesOrder,
            newItem: SalesOrder
        ): Boolean {
            return oldItem.soId == newItem.soId
        }

        override fun areContentsTheSame(
            oldItem: SalesOrder,
            newItem: SalesOrder
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class CellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSOIdSalesOrderRecord: TextView =
            itemView.findViewById(R.id.tvSOIdSalesOrderRecord)
        val tvSODateSalesOrderRecord: TextView =
            itemView.findViewById(R.id.tvSODateSalesOrderRecord)
        val tvBGpNameSalesOrderRecord: TextView =
            itemView.findViewById(R.id.tvBGpNameSalesOrderRecord)
        val tvBGcNameSalesOrderRecord: TextView =
            itemView.findViewById(R.id.tvBGcNameSalesOrderRecord)
        val tvBGdNameSalesOrderRecord: TextView =
            itemView.findViewById(R.id.tvBGdNameSalesOrderRecord)
        val tvCINameSalesOrderRecord: TextView =
            itemView.findViewById(R.id.tvCINameSalesOrderRecord)
        val tvCITelSalesOrderRecord: TextView =
            itemView.findViewById(R.id.tvCITelSalesOrderRecord)
        val tvCITel2SalesOrderRecord: TextView =
            itemView.findViewById(R.id.tvCITel2SalesOrderRecord)
        val tvCITel3SalesOrderRecord: TextView =
            itemView.findViewById(R.id.tvCITel3SalesOrderRecord)
        val layoutMainSalesOrderRecord: ConstraintLayout =
            itemView.findViewById(R.id.layoutMainSalesOrderRecord)
    }

    interface OnItemCheckedListener {
        fun onItemChecked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_sales_order_record, parent, false)
            .apply {
                return CellViewHolder(this)
            }
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val currentItem = getItem(position)

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        holder.tvSOIdSalesOrderRecord.text = currentItem.soId.toString()
        holder.tvSODateSalesOrderRecord.text = sdf.format(currentItem.soDate)
        holder.tvBGpNameSalesOrderRecord.text = currentItem.bGpName
        holder.tvBGcNameSalesOrderRecord.text = currentItem.bGcName
        holder.tvBGdNameSalesOrderRecord.text = currentItem.bGdName
        holder.tvCINameSalesOrderRecord.text = currentItem.ciName
        holder.tvCITelSalesOrderRecord.text = currentItem.ciTel
        holder.tvCITel2SalesOrderRecord.text = currentItem.ciTel2
        holder.tvCITel3SalesOrderRecord.text = currentItem.ciTel3

        holder.layoutMainSalesOrderRecord.setOnClickListener {
            listener.onItemChecked(position)
        }
    }
}
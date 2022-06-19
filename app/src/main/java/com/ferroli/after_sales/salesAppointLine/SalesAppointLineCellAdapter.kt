package com.ferroli.after_sales.salesAppointLine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ferroli.after_sales.R
import com.ferroli.after_sales.entity.SalesAppointLine
import com.ferroli.after_sales.salesOrder.SalesOrderCellAdapter
import java.text.SimpleDateFormat
import java.util.*

class SalesAppointLineCellAdapter :
    ListAdapter<SalesAppointLine, SalesAppointLineCellAdapter.CellViewHolder>(
        DIFFCALLBACK
    ) {
    object DIFFCALLBACK : DiffUtil.ItemCallback<SalesAppointLine>() {
        override fun areItemsTheSame(
            oldItem: SalesAppointLine,
            newItem: SalesAppointLine
        ): Boolean {
            return oldItem.sAlId == newItem.sAlId
        }

        override fun areContentsTheSame(
            oldItem: SalesAppointLine,
            newItem: SalesAppointLine
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class CellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivStateSalesAppointLineRecord: ImageView =
            itemView.findViewById(R.id.ivStateSalesAppointLineRecord)
        val tvStateSalesAppointLineRecord: TextView =
            itemView.findViewById(R.id.tvStateSalesAppointLineRecord)
        val tvBGpNameSalesAppointLineRecord: TextView =
            itemView.findViewById(R.id.tvBGpNameSalesAppointLineRecord)
        val tvSalDateSalesAppointLineRecord: TextView =
            itemView.findViewById(R.id.tvSalDateSalesAppointLineRecord)
        val tvFromEmpNameSalesAppointLineRecord: TextView =
            itemView.findViewById(R.id.tvFromEmpNameSalesAppointLineRecord)
        val tvSymbolSalesAppointLineRecord: TextView =
            itemView.findViewById(R.id.tvSymbolSalesAppointLineRecord)
        val tvToEmpNameSalesAppointLineRecord: TextView =
            itemView.findViewById(R.id.tvToEmpNameSalesAppointLineRecord)
        val tvSAlRemarkSalesAppointLineRecord: TextView =
            itemView.findViewById(R.id.tvSAlRemarkSalesAppointLineRecord)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_sales_appoint_line_record, parent, false)
            .apply {
                return CellViewHolder(this)
            }
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val currentItem = getItem(position)

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        if (currentItem.sAlIsError) {
            holder.tvStateSalesAppointLineRecord.text = "异常"

            holder.ivStateSalesAppointLineRecord.setImageResource(R.drawable.ic_error)
//            holder.ivStateSalesAppointLineRecord.setColorFilter(R.color.ferroli_remind_color)
            holder.tvFromEmpNameSalesAppointLineRecord.text = currentItem.fromEmpName
        } else if (currentItem.sAlIsTakeOrder) {
            holder.tvStateSalesAppointLineRecord.text = "接单"

            holder.ivStateSalesAppointLineRecord.setImageResource(R.drawable.ic_check)
//            holder.ivStateSalesAppointLineRecord.setColorFilter(R.color.ferroli_remind_3_color)
            holder.tvFromEmpNameSalesAppointLineRecord.text = ""
            holder.tvSymbolSalesAppointLineRecord.text = ""
        } else {
            holder.tvStateSalesAppointLineRecord.text = "派单"

            holder.ivStateSalesAppointLineRecord.setImageResource(R.drawable.ic_sales_order_appoint)
//            holder.ivStateSalesAppointLineRecord.setColorFilter(R.color.gray_600)
            holder.tvFromEmpNameSalesAppointLineRecord.text = currentItem.fromEmpName
        }

        holder.tvBGpNameSalesAppointLineRecord.text = currentItem.bGpName
        holder.tvToEmpNameSalesAppointLineRecord.text = currentItem.toEmpName
        holder.tvSAlRemarkSalesAppointLineRecord.text = currentItem.sAlRemark
        holder.tvSalDateSalesAppointLineRecord.text = sdf.format(currentItem.sAlDate)
    }
}
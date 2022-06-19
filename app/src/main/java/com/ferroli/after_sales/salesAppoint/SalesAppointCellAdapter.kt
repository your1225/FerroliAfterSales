package com.ferroli.after_sales.salesAppoint

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
import java.text.SimpleDateFormat
import java.util.*

class SalesAppointCellAdapter(private val listener: OnItemCheckedListener):
    ListAdapter<SalesAppointLine, SalesAppointCellAdapter.CellViewHolder>(
        DIFFCALLBACK
    )  {
    object DIFFCALLBACK : DiffUtil.ItemCallback<SalesAppointLine>() {
        override fun areItemsTheSame(
            oldItem: SalesAppointLine,
            newItem: SalesAppointLine
        ): Boolean {
            return oldItem.saId == newItem.saId
        }

        override fun areContentsTheSame(
            oldItem: SalesAppointLine,
            newItem: SalesAppointLine
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemCheckedListener {
        fun onItemChecked(position: Int)
    }

    inner class CellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layoutMainSalesAppointRecord: ConstraintLayout =
            itemView.findViewById(R.id.layoutMainSalesAppointRecord)
        val tvSAIdSalesAppointRecord: TextView =
            itemView.findViewById(R.id.tvSAIdSalesAppointRecord)
        val tvSADateSalesAppointRecord: TextView =
            itemView.findViewById(R.id.tvSAlDateSalesAppointRecord)
        val tvBScNameSalesAppointRecord: TextView =
            itemView.findViewById(R.id.tvBScNameSalesAppointRecord)
        val tvBGpNameSalesAppointRecord: TextView =
            itemView.findViewById(R.id.tvBGpNameSalesAppointRecord)
        val tvBGcNameSalesAppointRecord: TextView =
            itemView.findViewById(R.id.tvBGcNameSalesAppointRecord)
        val tvBGdNameSalesAppointRecord: TextView =
            itemView.findViewById(R.id.tvBGdNameSalesAppointRecord)
        val tvCINameSalesAppointRecord: TextView =
            itemView.findViewById(R.id.tvCINameSalesAppointRecord)
        val tvCITelSalesAppointRecord: TextView =
            itemView.findViewById(R.id.tvCITelSalesAppointRecord)
        val tvCITel2SalesAppointRecord: TextView =
            itemView.findViewById(R.id.tvCITel2SalesAppointRecord)
        val tvCITel3SalesAppointRecord: TextView =
            itemView.findViewById(R.id.tvCITel3SalesAppointRecord)
        val tvStateSalesAppointRecord: TextView =
            itemView.findViewById(R.id.tvStateSalesAppointRecord)
        val ivStateSalesAppointRecord: ImageView =
            itemView.findViewById(R.id.ivStateSalesAppointRecord)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_sales_appoint_record, parent, false)
            .apply {
                return CellViewHolder(this)
            }
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val currentItem = getItem(position)

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        if (currentItem.sAlIsError) {
            holder.tvStateSalesAppointRecord.text = "异常"

            holder.ivStateSalesAppointRecord.setImageResource(R.drawable.ic_error)
//            holder.ivStateSalesAppointRecord.setColorFilter(R.color.ferroli_remind_color)
        } else if (currentItem.sAlIsTakeOrder) {
            holder.tvStateSalesAppointRecord.text = "接单"

            holder.ivStateSalesAppointRecord.setImageResource(R.drawable.ic_check)
//            holder.ivStateSalesAppointRecord.setColorFilter(R.color.ferroli_remind_3_color)
        } else {
            holder.tvStateSalesAppointRecord.text = "派单"

            holder.ivStateSalesAppointRecord.setImageResource(R.drawable.ic_sales_order_appoint)
//            holder.ivStateSalesAppointRecord.setColorFilter(R.color.gray_600)
        }

        holder.tvSAIdSalesAppointRecord.text = currentItem.saId.toString()
        holder.tvSADateSalesAppointRecord.text = sdf.format(currentItem.sAlDate)
        holder.tvBScNameSalesAppointRecord.text = currentItem.bScName
        holder.tvBGpNameSalesAppointRecord.text = currentItem.bGpName
        holder.tvBGcNameSalesAppointRecord.text = currentItem.bGcName
        holder.tvBGdNameSalesAppointRecord.text = currentItem.bGdName
        holder.tvCINameSalesAppointRecord.text = currentItem.ciName
        holder.tvCITelSalesAppointRecord.text = currentItem.ciTel
        holder.tvCITel2SalesAppointRecord.text = currentItem.ciTel2
        holder.tvCITel3SalesAppointRecord.text = currentItem.ciTel3

        holder.layoutMainSalesAppointRecord.setOnClickListener {
            listener.onItemChecked(position)
        }
    }
}
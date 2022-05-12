package com.ferroli.after_sales.shareFileView

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
import com.ferroli.after_sales.entity.CusServiceFile

class ShareFileViewCellAdapter(private val listener: ShareFileViewCellAdapter.OnItemCheckedListener) :
    ListAdapter<CusServiceFile, ShareFileViewCellAdapter.CellViewHolder>(
        DIFFCALLBACK
    ) {
    object DIFFCALLBACK : DiffUtil.ItemCallback<CusServiceFile>() {
        override fun areItemsTheSame(
            oldItem: CusServiceFile,
            newItem: CusServiceFile
        ): Boolean {
            return oldItem.fileName == newItem.fileName
        }

        override fun areContentsTheSame(
            oldItem: CusServiceFile,
            newItem: CusServiceFile
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class CellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCellShareFileViewRecord: ImageView =
            itemView.findViewById(R.id.imgCellShareFileViewRecord)
        val tvNameCellShareFileViewRecord: TextView =
            itemView.findViewById(R.id.tvNameCellShareFileViewRecord)
        val tvFileCountCellShareFileViewRecord: TextView =
            itemView.findViewById(R.id.tvFileCountCellShareFileViewRecord)
        val layoutCellShareFileViewRecord: ConstraintLayout =
            itemView.findViewById(R.id.layoutCellShareFileViewRecord)
    }

    interface OnItemCheckedListener {
        fun onItemChecked(selectedFile: CusServiceFile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_share_file_view_record, parent, false)
            .apply {
                return CellViewHolder(this)
            }
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val currentItem = getItem(position)

//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        if (currentItem.isFolder) {
            holder.imgCellShareFileViewRecord.setImageResource(R.drawable.ic_folder_open)
            holder.tvFileCountCellShareFileViewRecord.text = currentItem.fileCount.toString()
        } else {
            holder.imgCellShareFileViewRecord.setImageResource(R.drawable.ic_insert_drive_file)
            holder.tvFileCountCellShareFileViewRecord.text = ""
        }

        holder.tvNameCellShareFileViewRecord.text = currentItem.fileName

        holder.layoutCellShareFileViewRecord.setOnClickListener {
            listener.onItemChecked(currentItem)
        }
    }
}
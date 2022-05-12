package com.ferroli.after_sales.entity

data class CusServiceFile(
    // 是否文件夹
    val isFolder: Boolean,
    // 文件名
    val fileName: String,
    // 文件个数
    val fileCount: Int,
    // 文件路径
    val filePath: String,
    // 子文件
    val childList: List<CusServiceFile>
)

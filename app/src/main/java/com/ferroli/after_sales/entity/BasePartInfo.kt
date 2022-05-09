package com.ferroli.after_sales.entity

data class BasePartInfo(
    // 配件种类id
    val bPcId: Int,
    // 配件编号
    val bPaiCode: String,
    // 配件名称
    val bPaiName: String,
    // 代理价
    val bPaiAgentPrice: Float,
    // 零售价
    val bPaiPrice: Float,
    // 备注
    val bPaiRemark: String,
    // 配件种类名称
    val bPcName: String
)

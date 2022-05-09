package com.ferroli.after_sales.entity

data class AgentOrderLine(
    // 网点采购ID
    val aoId: Int,
    // 网点采购行
    val aOlId: Int,
    // 配件ID
    val bPaiCode: String,
    // 数量
    val aOlCount: Int,
    // 代理价
    val aOlAgentPrice: Float,
    // 零售价
    val aOlPrice: Float,
    // 配件类型
    val bPcId: Int,
    // 配件名称
    val bPaiName: String,
    // 配件类型名称
    val bPcName: String,
    // 网点ID
    val aoReceiveEmpId: Int,
    // 是否已经审批
    val aoIsApprove: Boolean,
    // 是否已经发货
    val aoIsSend: Boolean,
    // 审批数量
    val aOlApproveCount: Int,
    // 审批备注
    val aOlApproveRemark: String,
    // 库存
    val stockCount: Int
)

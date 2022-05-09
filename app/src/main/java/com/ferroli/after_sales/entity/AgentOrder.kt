package com.ferroli.after_sales.entity

import java.util.*

data class AgentOrder(
    // 网点采购ID
    val aoId: Int,
    // 网点ID
    val aoReceiveEmpId: Int,
    // 收件人
    val aoReceiveName: String,
    // 收件人电话
    val aoReceiveTel: String,
    // 收件人地址
    val aoReceiveAddress: String,
    // 填单人
    val aoEmpId: Int,
    // 填单日期
    val aoDate: Date,
    // 备注
    val aoRemark: String,
    // 是否审批
    val aoIsApprove: Boolean,
    // 审批日期
    val aoApproveDate: Date,
    // 审批人
    val aoApproveEmpId: Int,
    // 是否发货
    val aoIsSend: Boolean,
    // 发货日期
    val aoSendDate: Date,
    // 发货人
    val aoSendEmpId: Int,
    // 审批备注
    val aoApproveRemark: String,
    // 订购网点
    val agentName: String,
    // 填单人
    val empName: String,
    // 审批人
    val approveEmpName: String,
    // 发货人
    val sendEmpName: String
)

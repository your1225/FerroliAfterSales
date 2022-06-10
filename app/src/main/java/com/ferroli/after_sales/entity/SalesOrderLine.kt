package com.ferroli.after_sales.entity

data class SalesOrderLine (
    val soId: Int,
    val sOlId: Int,
    val bPiCode: String,
    val sOlNum: Int,
    val sOlMoney: Float,
    val sOlRemark: String,
    val bPtId: Int,
    val bPmId: Int,
    val bPtName: String,
    val bPmName: String,
    val bPiName: String,
    val bPiPrice: Float,
    val bPiRemark: String,
    val bPiEName: String,
    val bPiOffline: Boolean,
    val displayName: String
)
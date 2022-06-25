package com.ferroli.after_sales.entity

import java.util.*

data class SalesFinishLine(
    val sfId: Int,
    val sFlId: Int,
    val bPcId: Int,
    val bPaiCode: String,
    val sFlBarcode: String,
    val sfDate: Date,
    val userName: String,
    val sFlCount: Int,
    val sFlAgentPrice: Float,
    val sFlPrice: Float,
    val bPaiName: String,
    val bPcName: String,
    val agentTotalMoney: Float,
    val sfEmpId: Int
)

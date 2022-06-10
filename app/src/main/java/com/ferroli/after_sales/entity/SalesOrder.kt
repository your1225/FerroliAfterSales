package com.ferroli.after_sales.entity

import java.util.*

data class SalesOrder(
    val soId: Int,
    val ciId: Int,
    val soPurchaseDate: Date,
    val soAppointmentDate: Date,
    val soRemark: String,
    val bGpId: Int,
    val bGcId: Int,
    val bGdId: Int,
    val ciName: String,
    val ciTel: String,
    val ciTel2: String,
    val ciTel3: String,
    val ciRemark: String,
    val bGpName: String,
    val bGcName: String,
    val bGdName: String,
    val soEmpId: Int,
    val soDate: Date,
    val userName: String,
    val ciAddress: String,
    val soIsAppoint: Boolean,
    val saId: Int,
    val soIsFinish: Boolean,
    val sfId: Int
)

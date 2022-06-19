package com.ferroli.after_sales.entity

import java.util.*

data class SalesAppoint(
    val saId: Int,
    val soId: Int,
    val bScId: Int,
    val saEmpId: Int,
    val saDate: Date,
    val saIsComplate: Boolean,
    val saComplateDate: Date,
    val saRemark: String,
    val bScName: String,
    val userName: String,
    val ciName: String,
    val ciTel: String,
    val bGpName: String,
    val saIsFinish: Boolean,
    val sfId: Int,
    val saIsAgainAppoint: Boolean,
    val bGpId: Int,
    val soEmpId: Int,
    val cusNo: String,
    val cusName: String,
    val bGcName: String,
    val bGdName: String,
    val soUserName: String,
    val ciTel2: String,
    val ciTel3: String
)

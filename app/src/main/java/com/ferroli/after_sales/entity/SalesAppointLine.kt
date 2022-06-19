package com.ferroli.after_sales.entity

import java.util.*

data class SalesAppointLine(
    val saId: Int,
    val sAlId: Int,
    val sAlAppointDate: Date,
    val sAlFromEmpId: Int,
    val sAlToEmpId: Int,
    val sAlDate: Date,
    val sAlIsTakeOrder: Boolean,
    val sAlTakeOrderDate: Date,
    val sAlIsCurrent: Boolean,
    val sAlRemark: String,
    val fromEmpName: String,
    val toEmpName: String,
    val soId: Int,
    val ciName: String,
    val ciTel: String,
    val bGpName: String,
    val saIsComplate: Boolean,
    val saComplateDate: Date,
    val toEmpDisplayText: String,
    val sAlIsError: Boolean,
    val saIsAgainAppoint: Boolean,
    val bGcName: String,
    val bGdName: String,
    val soUserName: String,
    val ciTel2: String,
    val ciTel3: String,
    val bScName: String
)

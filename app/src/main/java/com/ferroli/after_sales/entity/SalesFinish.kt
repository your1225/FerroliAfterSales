package com.ferroli.after_sales.entity

import java.util.*

data class SalesFinish(
    val sfId: Int,
    val pcBarcode: String,
    val bPiCode: String,
    val sfUnderWarranty: Boolean,
    val sfFinishDate: Date,
    val soId: Int,
    val saId: Int,
    val sfEmpId: Int,
    val sfDate: Date,
    val sfRemark: String,
    val sfIsRevisit: Boolean,
    val srId: Int,
    val userName: String,
    val ciId: Int,
    val bStId: Int,
    val bStName: String,
    val bGpName: String,
    val bGcName: String,
    val bGdName: String,
    val ciTel: String,
    val ciName: String,
    val sfNeedApprove: Boolean,
    val sfIsApprove: Boolean,
    val sfApproveDate: Date,
    val sfApproveEmpId: Int,
    val approveEmpName: String,
    val sfIsExamine: Boolean,
    val sfIsLineExamine: Boolean,
    val seId: Int,
    val bSmMoney: Int,
    val ciAddress: String,
    val cusName: String,
    val srIsSolve: Boolean,
    val bPiName: String,
    val bPiEName: String,
    val bGpId: Int,
    val productDisplayName: String,
    val cusNo: String,
    val soDate: Date,
    val soRemark: String,
    val ciTel2: String,
    val ciTel3: String,
    val soUserName: String
)

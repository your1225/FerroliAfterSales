package com.ferroli.after_sales.entity

data class BaseProductInfo(
    // 产品编码
    val bPiCode: String,
    // 产品品牌ID
    val bPtId: Int,
    // 产品型号ID
    val bPmId: Int,
    // 产品名称
    val bPiName: String,
    // 产品英文名
    val bPiEName: String,
    // 是否线下
    val bPiOffline: Boolean,
    // 零售价
    val bPiPrice: Float,
    // 备注
    val bPiRemark: String,
    // 品牌
    val bPtName: String,
    // 类型
    val bPmName: String,
    // 显示名称
    val displayName: String,
    // 功率(千瓦)
    val bPiPower: Float,
    // 保修期(月)
    val bPiWarranty: Int,
    // 可否延保
    val bPiCanAddWarranty: Boolean,
    // 延保多久
    val bPiAddLength: Int
)

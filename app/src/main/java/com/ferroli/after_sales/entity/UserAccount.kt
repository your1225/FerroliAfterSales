package com.ferroli.after_sales.entity

data class UserAccount(
    // 用户ID
    val empId: Int,
    // 用户名
    val userName: String,
    // 可使用APP
    val appName: String,
    // 可使用窗体
    val frmList: String,
    // 父类们
    val parentList: String,
    // 网点编码
    val cusNo: String,
    // 网点名称
    val cusName: String,
    // Ding ID
    val dingId: String
)

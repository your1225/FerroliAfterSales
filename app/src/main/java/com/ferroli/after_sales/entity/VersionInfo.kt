package com.ferroli.after_sales.entity

data class VersionInfo(
    // 版本ID
    val viId: Int,
    // 项目名称
    val viProjectName: String,
    // 版本号
    val viVerName: String,
    // 版本标题
    val viTitle: String,
    // 版本消息
    val viMsg: String,
    // 是否强制
    val viIsForce: Boolean,
    // 下载地址
    val viUrl: String
)

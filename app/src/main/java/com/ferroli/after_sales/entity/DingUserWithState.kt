package com.ferroli.after_sales.entity

data class DingUserWithState(
    val empId: Int,
    val userName: String,
    val cusNo: String,
    val cusName: String,
    val avatar: String,
    val stateCount1: Int,
    val stateCount2: Int,
    val stateCount3: Int,
    val stateCount4: Int
)

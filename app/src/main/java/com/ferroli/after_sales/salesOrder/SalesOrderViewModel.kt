package com.ferroli.after_sales.salesOrder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ferroli.after_sales.entity.BaseProductInfo

class SalesOrderViewModel(application: Application) : AndroidViewModel(application) {
    // 物品列表
    private var _baseProductInfoRecord = MutableLiveData<List<BaseProductInfo>?>()
    var baseProductInfoRecord: LiveData<List<BaseProductInfo>?> = _baseProductInfoRecord



    fun addProductInfo(pi: BaseProductInfo)    {
        _baseProductInfoRecord.value = _baseProductInfoRecord.value?.plus(pi) ?: listOf(pi)
    }

}
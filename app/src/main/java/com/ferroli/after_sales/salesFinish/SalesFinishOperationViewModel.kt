package com.ferroli.after_sales.salesFinish

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ferroli.after_sales.entity.AgentOrderLine
import com.ferroli.after_sales.entity.BasePartInfo
import com.ferroli.after_sales.utils.LoginInfo

class SalesFinishOperationViewModel(application: Application) : AndroidViewModel(application) {

    private var _basePartInfoRecord = MutableLiveData<List<AgentOrderLine>?>()

    // 选择的物品
    var basePartInfoRecord: LiveData<List<AgentOrderLine>?> = _basePartInfoRecord

    fun addBasePartInfo(item: BasePartInfo, buyCount: Int) {
        val line = AgentOrderLine(
            aoId = -1,
            aOlId = -1,
            bPaiCode = item.bPaiCode,
            aOlCount = buyCount,
            aOlAgentPrice = item.bPaiAgentPrice,
            aOlPrice = item.bPaiPrice,
            bPcId = item.bPcId,
            bPaiName = item.bPaiName,
            bPcName = item.bPcName,
            aoReceiveEmpId = LoginInfo.getLoginEmpId(getApplication()),
            aoIsApprove = false,
            aoIsSend = false,
            aOlApproveCount = 0,
            aOlApproveRemark = "",
            stockCount = 0
        )

        _basePartInfoRecord.value = _basePartInfoRecord.value?.plus(line) ?: listOf(line)
    }

}
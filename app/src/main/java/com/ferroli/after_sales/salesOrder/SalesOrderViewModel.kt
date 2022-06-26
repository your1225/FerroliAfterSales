package com.ferroli.after_sales.salesOrder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.ferroli.after_sales.R
import com.ferroli.after_sales.entity.*
import com.ferroli.after_sales.utils.DateDeserializer
import com.ferroli.after_sales.utils.LoginInfo
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class SalesOrderViewModel(application: Application) : AndroidViewModel(application) {

    // 省/市/区 列表
    private var _baseGeoProvinceRecord = MutableLiveData<List<BaseGeoProvince>?>()
    private var _baseGeoCityRecord = MutableLiveData<List<BaseGeoCity>?>()
    private var _baseGeoDistrictRecord = MutableLiveData<List<BaseGeoDistrict>?>()

    var baseGeoProvinceRecord: LiveData<List<BaseGeoProvince>?> = _baseGeoProvinceRecord
    var baseGeoCityRecord: LiveData<List<BaseGeoCity>?> = _baseGeoCityRecord
    var baseGeoDistrict: LiveData<List<BaseGeoDistrict>?> = _baseGeoDistrictRecord

    private var cityAll: List<BaseGeoCity> = arrayListOf()
    private var districtAll: List<BaseGeoDistrict> = arrayListOf()

    // 所选产品
    private var _baseProductInfoRecord = MutableLiveData<BaseProductInfo?>()
    var baseProductInfoRecord: LiveData<BaseProductInfo?> = _baseProductInfoRecord

    // 购买日期
    val soPurchaseDate = MutableLiveData<Date>()
    // 预约日期
    val soAppointmentDate = MutableLiveData<Date>()

    // 提示信息
    var remarkText = MutableLiveData<String>()

    // 返回值
    var reData = MutableLiveData<SysSqlReturn>()

    fun getProvince() {
        val url = urlBase + "Base/BaseGeoProvince_GetModelList/1=1"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    _baseGeoProvinceRecord.value =
                        gson.fromJson(it, Array<BaseGeoProvince>::class.java).toList()
                } else {
                    remarkText.value = "未找到省份信息"
                }
            },
            {
                remarkText.value =
                    getApplication<Application>().resources.getString(R.string.app_error)
//                Log.d("Ferroli Log", it.toString())
            }
        ).also {
            VolleySingleton.getInstance(getApplication()).requestQueue.add(it)
        }
    }

    fun getCity() {
        val url = urlBase + "Base/BaseGeoCity_GetModelList/1=1"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    cityAll = gson.fromJson(it, Array<BaseGeoCity>::class.java).toList()
                } else {
                    remarkText.value = "未找到城市信息"
                }
            },
            {
                remarkText.value =
                    getApplication<Application>().resources.getString(R.string.app_error)
//                Log.d("Ferroli Log", it.toString())
            }
        ).also {
            VolleySingleton.getInstance(getApplication()).requestQueue.add(it)
        }
    }

    fun getDistrict() {
        val url = urlBase + "Base/BaseGeoDistrict_GetModelList/1=1"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    districtAll = gson.fromJson(it, Array<BaseGeoDistrict>::class.java).toList()
                } else {
                    remarkText.value = "未找到地区信息"
                }
            },
            {
                remarkText.value =
                    getApplication<Application>().resources.getString(R.string.app_error)
//                Log.d("Ferroli Log", it.toString())
            }
        ).also {
            VolleySingleton.getInstance(getApplication()).requestQueue.add(it)
        }
    }

    fun addProductInfo(bPiCode: String) {
        val url = urlBase + "ProduceInfo/GetModelByCode/${bPiCode}"

        StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.isNotEmpty() && it != "null") {
                    val gson =
                        GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer())
                            .create()

                    val pi = gson.fromJson(it, BaseProductInfo::class.java)

                    _baseProductInfoRecord.value = pi
                } else {
                    remarkText.value = "未找到成品信息"
                }
            },
            {
                remarkText.value =
                    getApplication<Application>().resources.getString(R.string.app_error)
//                Log.d("Ferroli Log", it.toString())
            }
        ).also {
            VolleySingleton.getInstance(getApplication()).requestQueue.add(it)
        }
    }

    fun filterCity(position: Int) {
        baseGeoProvinceRecord.value?.get(position)?.run {
            _baseGeoCityRecord.value = cityAll.filter { it.bGpId == this.bGpId }
        }
    }

    fun filterDistrict(position: Int) {
        baseGeoCityRecord.value?.get(position)?.run {
            _baseGeoDistrictRecord.value = districtAll.filter { it.bGcId == this.bGcId }
        }
    }

    fun saveData(so: SalesOrder, solMoney: Float) {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val url = urlBase + "SalesOrder/Add"

        val empId = LoginInfo.getLoginEmpId(getApplication())
//        val empId = 1225

        val array = JSONArray()

        baseProductInfoRecord.value?.run {
            val obj = JSONObject()
            obj.put("SOId", -1)
            obj.put("SOlId", -1)
            obj.put("BPiCode", baseProductInfoRecord.value?.bPiCode)
            obj.put("SOlNum", 1)
            obj.put("SOlMoney", solMoney)
            obj.put("SOlRemark", "")

            array.put(obj)
        }

        // 转换一下，从位置拿到位置的ID
        val bgpId = baseGeoProvinceRecord.value?.get(so.bGpId)?.bGpId
        val bGcId = baseGeoCityRecord.value?.get(so.bGcId)?.bGcId
        val bGdId = baseGeoDistrict.value?.get(so.bGdId)?.bGdId

        val jsonObject = JSONObject()
        jsonObject.put("SOId", so.soId)
        jsonObject.put("CIId", so.ciId)
        jsonObject.put("SOPurchaseDate", formatter.format(so.soPurchaseDate))
        jsonObject.put("SOAppointmentDate", formatter.format(so.soAppointmentDate))
        jsonObject.put("SORemark", so.soRemark)
        jsonObject.put("BGpId", bgpId ?: 0)
        jsonObject.put("BGcId", bGcId ?: 0)
        jsonObject.put("BGdId", bGdId ?: 0)
        jsonObject.put("CIName", so.ciName)
        jsonObject.put("CITel", so.ciTel)
        jsonObject.put("CITel2", so.ciTel2)
        jsonObject.put("CITel3", so.ciTel3)
        jsonObject.put("CIRemark", "")
        jsonObject.put("SOEmpId", empId)
        jsonObject.put("CIAddress", so.ciAddress)

        jsonObject.put("Lines", array)

        object : JsonObjectRequest(
            Method.POST,
            url,
            jsonObject,
            Response.Listener {
                if (it != null) {
                    reData.value = SysSqlReturn(it.getString("fOK"), it.getString("fMsg"))
                } else {
                    remarkText.value =
                        getApplication<Application>().resources.getString(R.string.app_error)
                }
            },
            Response.ErrorListener {
                remarkText.value =
                    getApplication<Application>().resources.getString(R.string.app_error)
//                Log.d("Ferroli Log", it.toString())
            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json"
            }
        }.also {
            VolleySingleton.getInstance(getApplication()).requestQueue.add(it)
        }
    }

    fun clearData() {
        _baseProductInfoRecord.postValue(null)
    }
}
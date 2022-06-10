package com.ferroli.after_sales.salesOrder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import java.util.*

class SalesOrderViewModel(application: Application) : AndroidViewModel(application) {

    // 省/市/区
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

    fun saveData(so: SalesOrder, pi: BaseProductInfo, solMoney: Float) {
        val url = urlBase + "AgentOrder/Post"

        val empId = LoginInfo.getLoginEmpId(getApplication())
//        val empId = 1225

        val array = JSONArray()

        baseProductInfoRecord.value?.run {
            val obj = JSONObject()
            obj.put("soId", -1)
            obj.put("sOlId", -1)
            obj.put("bPiCode", pi.bPiCode)
            obj.put("sOlNum", 1)
            obj.put("sOlMoney", solMoney)
            obj.put("sOlRemark", "")

            array.put(obj)
        }

        val jsonObject = JSONObject()
        jsonObject.put("soId", so.soId)
        jsonObject.put("ciId", so.ciId)
        jsonObject.put("soPurchaseDate", so.soPurchaseDate)
        jsonObject.put("soAppointmentDate", so.soAppointmentDate)
        jsonObject.put("soRemark", so.soRemark)
        jsonObject.put("bGpId", so.bGpId)
        jsonObject.put("bGcId", so.bGcId)
        jsonObject.put("bGdId", so.bGdId)
        jsonObject.put("ciName", so.ciName)
        jsonObject.put("ciTel", so.ciTel)
        jsonObject.put("ciTel2", so.ciTel2)
        jsonObject.put("ciTel3", so.ciTel3)
        jsonObject.put("ciRemark", so.ciRemark)
        jsonObject.put("soEmpId", empId)
        jsonObject.put("ciAddress", so.ciAddress)

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
}
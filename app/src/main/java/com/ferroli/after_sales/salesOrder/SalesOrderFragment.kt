package com.ferroli.after_sales.salesOrder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ferroli.after_sales.R
import com.ferroli.after_sales.databinding.FragmentSalesOrderBinding
import com.ferroli.after_sales.entity.SalesOrder
import com.ferroli.after_sales.utils.ToastUtil
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class SalesOrderFragment : Fragment() {

    private lateinit var binding: FragmentSalesOrderBinding
    private val viewModel by activityViewModels<SalesOrderViewModel>()
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalesOrderBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        viewModel.getProvince()
        viewModel.getCity()
        viewModel.getDistrict()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("bPiCode")
            ?.observe(viewLifecycleOwner) {
                viewModel.addProductInfo(it)
            }

        binding.btnAddSalesOrder.setOnClickListener {
            findNavController().navigate(
                R.id.action_salesOrderFragment_to_baseProductInfoFragment
            )
        }

        viewModel.baseProductInfoRecord.observe(viewLifecycleOwner) {
            it?.run {
                binding.tvBPtNameSalesOrder.text = this.bPtName
                binding.tvBPmNameSalesOrder.text = this.bPmName
                binding.tvBPiNameSalesOrder.text = this.bPiName
                binding.tvBPiENameSalesOrder.text = this.bPiEName
            }
        }
        viewModel.remarkText.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                ToastUtil.showToast(requireContext(), it)
            }
        }
        viewModel.reData.observe(viewLifecycleOwner) {
            when (it?.fOK) {
                "True" -> {
//                    Log.e("ferroli_log",
//                        "reData observe " + viewModel.ckNoLiveData.value + " - " + viewModel.itmLiveData.value)

                    viewModel.remarkText.postValue("已经保存成功!")

                    clearData()
                }
                "False" -> {
                    viewModel.remarkText.postValue(it.fMsg)
                }
            }

            binding.btnSaveSalesOrder.isEnabled = true
        }

        binding.layout9SalesOrder.setEndIconOnClickListener {
            val selectedLen = Date().time

            val materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.sales_order_hint_14)
                .setSelection(selectedLen)
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setStart(Date().time)
                        .build()
                )
                .build()

            materialDatePicker.show(requireActivity().supportFragmentManager, "DatePickerDialog")

            materialDatePicker.addOnPositiveButtonClickListener {
                viewModel.soPurchaseDate.value = Date(it)
            }
        }

        binding.layout10SalesOrder.setEndIconOnClickListener {
            val selectedLen = Date().time

            val materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.sales_order_hint_15)
                .setSelection(selectedLen)
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setStart(Date().time)
                        .build()
                )
                .build()

            materialDatePicker.show(requireActivity().supportFragmentManager, "DatePickerDialog")

            materialDatePicker.addOnPositiveButtonClickListener {
                viewModel.soAppointmentDate.value = Date(it)
            }
        }

        viewModel.baseGeoProvinceRecord.observe(viewLifecycleOwner) {
            if (it == null)
                return@observe

            val gpList: MutableList<String> = ArrayList()

            for (item in it) {
                gpList.add(item.bGpName)
            }

            val adapter = ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                gpList
            )

            binding.spnProvinceSalesOrder.adapter = adapter
        }

        viewModel.baseGeoCityRecord.observe(viewLifecycleOwner) {
            if (it == null)
                return@observe

            val gcList: MutableList<String> = ArrayList()

            for (item in it) {
                gcList.add(item.bGcName)
            }

            val adapter = ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                gcList
            )

            binding.spnCitySalesOrder.adapter = adapter
        }

        viewModel.baseGeoDistrict.observe(viewLifecycleOwner) {
            if (it == null)
                return@observe

            val gdList: MutableList<String> = ArrayList()

            for (item in it) {
                gdList.add(item.bGdName)
            }

            val adapter = ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                gdList
            )

            binding.spnDistrictSalesOrder.adapter = adapter
        }

        viewModel.soPurchaseDate.observe(viewLifecycleOwner) {
            binding.tbSOPurchaseDateSalesOrder.setText(formatter.format(it))
        }

        viewModel.soAppointmentDate.observe(viewLifecycleOwner) {
            binding.tbSOAppointmentDateSalesOrder.setText(formatter.format(it))
        }

        binding.spnProvinceSalesOrder.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.filterCity(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.spnCitySalesOrder.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.filterDistrict(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.btnSaveSalesOrder.setOnClickListener {
            if (viewModel.soPurchaseDate.value == null || viewModel.soAppointmentDate.value == null) {
                viewModel.remarkText.postValue("日期必须选择！")

                return@setOnClickListener
            }

            if (viewModel.baseProductInfoRecord.value == null) {
                viewModel.remarkText.postValue("必须选择产品！")

                return@setOnClickListener
            }

            if (binding.tbCINameSalesOrder.text.toString().isEmpty()) {
                viewModel.remarkText.postValue("必须填写姓名！")

                return@setOnClickListener
            }

            if (binding.tbCITelSalesOrder.text.toString().isEmpty()) {
                viewModel.remarkText.postValue("必须填写电话！")

                return@setOnClickListener
            }

            val so = SalesOrder(
                soId = -1,
                ciId = -1,
                soPurchaseDate = viewModel.soPurchaseDate.value!!,
                soAppointmentDate = viewModel.soAppointmentDate.value!!,
                soRemark = binding.tbSORemarkSalesOrder.text.toString(),
                bGpId = binding.spnProvinceSalesOrder.selectedItemPosition,
                bGcId = binding.spnCitySalesOrder.selectedItemPosition,
                bGdId = binding.spnDistrictSalesOrder.selectedItemPosition,
                ciName = binding.tbCINameSalesOrder.text.toString(),
                ciTel = binding.tbCITelSalesOrder.text.toString(),
                ciTel2 = binding.tbCITel2SalesOrder.text.toString(),
                ciTel3 = binding.tbCITel3SalesOrder.text.toString(),
                ciRemark = "",
                bGpName = "",
                bGcName = "",
                bGdName = "",
                soEmpId = -1,
                soDate = Date(),
                userName = "",
                ciAddress = binding.tbCIAddressSalesOrder.text.toString(),
                soIsAppoint = false,
                saId = -1,
                soIsFinish = false,
                sfId = -1
            )

            val solMoney: Float = binding.tbSOlMoneySalesOrder.text.toString().toFloat()

            binding.btnSaveSalesOrder.isEnabled = false

            viewModel.saveData(so, solMoney)
        }
    }

    private fun clearData() {
        binding.tbCINameSalesOrder.setText("")
        binding.tbCITelSalesOrder.setText("")
        binding.tbCITel2SalesOrder.setText("")
        binding.tbCITel3SalesOrder.setText("")
        binding.tbCIAddressSalesOrder.setText("")
        binding.tbSORemarkSalesOrder.setText("")

        binding.tvBPtNameSalesOrder.text = ""
        binding.tvBPmNameSalesOrder.text = ""
        binding.tvBPiNameSalesOrder.text = ""
        binding.tvBPiENameSalesOrder.text = ""

        viewModel.clearData()
    }
}
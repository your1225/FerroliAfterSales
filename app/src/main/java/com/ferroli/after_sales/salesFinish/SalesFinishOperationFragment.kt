package com.ferroli.after_sales.salesFinish

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ferroli.after_sales.R
import com.ferroli.after_sales.databinding.FragmentSalesFinishOperationBinding
import com.ferroli.after_sales.entity.SalesFinishLine
import com.ferroli.after_sales.utils.CameraActivityResultContract
import com.ferroli.after_sales.utils.ToastUtil
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class SalesFinishOperationFragment : Fragment(), SalesFinishCellAdapter.OnItemOperationListener {

    private lateinit var binding: FragmentSalesFinishOperationBinding
    private val viewModel by activityViewModels<SalesFinishOperationViewModel>()
    private val mmAdapter = SalesFinishCellAdapter(this)
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalesFinishOperationBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPartSalesFinishOperation.apply {
            adapter = mmAdapter
            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("bPiCode")
            ?.observe(viewLifecycleOwner) {
                viewModel.addProductInfo(it)
            }

        binding.btnProductFromSelectSalesFinishOperation.setOnClickListener {
            viewModel.qrCodePassed.value = false

            findNavController().navigate(
                R.id.action_salesFinishOperationFragment_to_baseProductInfoFragment
            )
        }
        binding.btnPartFromSelectSalesFinishOperation.setOnClickListener {
            val bundle = bundleOf(
                "FromFragment" to "SalesFinishOperation"
            )

            findNavController().navigate(
                R.id.action_salesFinishOperationFragment_to_basePartInfoFragment,
                bundle
            )
        }
        binding.tilQrCodeSalesFinishOperation.setEndIconOnClickListener {
            viewModel.qrCodePassed.value = false
            cameraActivityLauncher.launch("请扫描")
        }
        binding.btnSaveSalesFinishOperation.setOnClickListener {
            val pcBarcode: String = binding.tbQrCodeSalesFinishOperation.text.toString()
            val bStPosition: Int = binding.spServiceTypeSalesFinishOperation.selectedItemPosition
            val sfUnderWarranty: Boolean = binding.swInWarrantySalesFinishOperation.isChecked
            val sfRemark: String = binding.tbRemarkSalesFinishOperation.text.toString()
            val sfNeedApprove: Boolean = if (binding.rbTelSalesFinishOperation.isChecked) {
                false
            } else {
                viewModel.qrCodePassed.value?.let {
                    !it
                } ?: false
            }

            if (viewModel.baseProductInfoRecord.value == null) {
                viewModel.remarkText.postValue("必须选择产品")
                return@setOnClickListener
            }

            if (viewModel.sfFinishDate.value == null) {
                viewModel.remarkText.postValue("日期必须选择！")

                return@setOnClickListener
            }

            binding.btnSaveSalesFinishOperation.isEnabled = false

            viewModel.saveData(pcBarcode, bStPosition, sfUnderWarranty, sfRemark, sfNeedApprove)
        }
        binding.tilDateSalesFinishOperation.setEndIconOnClickListener {
            val selectedLen = Date().time

            val materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.sales_finish_hint_12)
                .setSelection(selectedLen)
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setStart(Date().time)
                        .build()
                )
                .build()

            materialDatePicker.show(requireActivity().supportFragmentManager, "DatePickerDialog")

            materialDatePicker.addOnPositiveButtonClickListener {
                viewModel.sfFinishDate.value = Date(it)
            }
        }

        viewModel.salesFinishLineRecord.observe(viewLifecycleOwner) {
            mmAdapter.submitList(it)
        }
        viewModel.baseProductInfoRecord.observe(viewLifecycleOwner) {
            it?.run {
                binding.tvBPtNameSalesFinishOperation.text = this.bPtName
                binding.tvBPmNameSalesFinishOperation.text = this.bPmName
                binding.tvBPiNameSalesFinishOperation.text = this.bPiName
                binding.tvBPiENameSalesFinishOperation.text = this.bPiEName
            }
        }
        viewModel.baseServiceTypeRecord.observe(viewLifecycleOwner) {
            if (it == null)
                return@observe

            val gpList: MutableList<String> = ArrayList()

            for (item in it) {
                gpList.add(item.bStName)
            }

            val adapter = ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                gpList
            )

            binding.spServiceTypeSalesFinishOperation.adapter = adapter
        }
        viewModel.reData.observe(viewLifecycleOwner) {
            binding.btnSaveSalesFinishOperation.isEnabled = true

            when (it?.fOK) {
                "True" -> {
//                    Log.e("ferroli_log",
//                        "reData observe " + viewModel.ckNoLiveData.value + " - " + viewModel.itmLiveData.value)

                    viewModel.remarkText.postValue("已经保存成功!")

                    clearData()

                    findNavController().popBackStack()
                }
                "False" -> {
                    viewModel.remarkText.postValue(it.fMsg)
                }
            }
        }
        viewModel.remarkText.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                ToastUtil.showToast(requireContext(), it)
            }
        }
        viewModel.sfFinishDate.observe(viewLifecycleOwner) {
            binding.tbDateSalesFinishOperation.setText(formatter.format(it))
        }
        viewModel.qrCodePassed.observe(viewLifecycleOwner) {
            binding.tvQrCodeStateSalesFinishOperation.text = if (it) "通过" else "须审批"
        }

        val selectedSa = arguments?.getString("saId", "-1")?.toInt() ?: -1

        viewModel.getBaseServiceType()
        viewModel.getSalesAppointLineCurrent(selectedSa)
    }

    private val cameraActivityLauncher =
        registerForActivityResult(CameraActivityResultContract()) { result ->
            if (result.isNotBlank() && result != "no data") {
                binding.tbQrCodeSalesFinishOperation.setText(result)

                viewModel.getBaseProductInfoByQrCode(result)
            }
        }

    override fun onDeleteClick(item: SalesFinishLine) {
        viewModel.removeBasePartInfo(item)
    }

    private fun clearData() {
        binding.tbQrCodeSalesFinishOperation.setText("")
        binding.tbRemarkSalesFinishOperation.setText("")

        viewModel.clearData()
    }
}
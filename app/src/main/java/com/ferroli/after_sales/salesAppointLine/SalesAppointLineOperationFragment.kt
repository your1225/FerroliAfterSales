package com.ferroli.after_sales.salesAppointLine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.R
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ferroli.after_sales.databinding.FragmentSalesAppointLineOperationBinding
import com.ferroli.after_sales.utils.DetailCellAdapter
import com.ferroli.after_sales.utils.ToastUtil
import java.util.ArrayList

class SalesAppointLineOperationFragment : Fragment() {

    private lateinit var binding: FragmentSalesAppointLineOperationBinding
    private val viewModel by viewModels<SalesAppointLineOperationViewModel>()

    private var isTakeOrder = false
    private var isError = false
    private var isComplete = false
    private var selectedSa: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalesAppointLineOperationBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val saAdapter = DetailCellAdapter()
        val salAdapter = SalesAppointLineCellAdapter()

        binding.rvSaDataSalesAppointLineOperation.apply {
            adapter = saAdapter
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }
        binding.rvSaLineSalesAppointLineOperation.apply {
            adapter = salAdapter
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }

        viewModel.detailInfoRecord.observe(viewLifecycleOwner) {
            saAdapter.submitList(it)
        }
        viewModel.saLineRecord.observe(viewLifecycleOwner) {
            salAdapter.submitList(it)
        }
        viewModel.appointUserRecord.observe(viewLifecycleOwner){
            if (it == null)
                return@observe

            val gdList: MutableList<String> = ArrayList()

            for (item in it) {
                if (item.cusName.isNotEmpty())
                    gdList.add(item.cusName)
                else
                    gdList.add(item.userName)
            }

            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                gdList
            )

            binding.spToEmpSalesAppointLineOperation.adapter = adapter
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

                    findNavController().popBackStack()
                }
                "False" -> {
                    viewModel.remarkText.postValue(it.fMsg)
                }
            }
        }

        binding.btnSaveSalesAppointLineOperation.setOnClickListener {
            if (binding.tbRemarkSalesAppointLineOperation.text.toString().isEmpty()) {
                viewModel.remarkText.postValue("必须填写备注！")

                return@setOnClickListener
            }

            if (!isComplete && !isError && !isTakeOrder) {
                if (binding.spToEmpSalesAppointLineOperation.selectedItemPosition == -1) {
                    viewModel.remarkText.postValue("必须选择委派给谁！")

                    return@setOnClickListener
                }
            }

            viewModel.saveData(
                toEmpPosition = binding.spToEmpSalesAppointLineOperation.selectedItemPosition,
                isTakeOrder = isTakeOrder,
                isError = isError,
                isComplete = isComplete,
                remark = binding.tbRemarkSalesAppointLineOperation.text.toString()
            )
        }
        binding.btnNextSalesAppointLineOperation.setOnClickListener {
            binding.layoutMainSalesAppointLineOperation.openDrawer(binding.layout3SalesAppointLineOperation)

            isTakeOrder = false
            isError = false
            isComplete = false

            binding.tvTitle3SalesAppointLineOperation.text = "任务委派"

            binding.btnSaveSalesAppointLineOperation.text = "任务委派"

            binding.tvTitle4SalesAppointLineOperatin.visibility = View.VISIBLE
            binding.spToEmpSalesAppointLineOperation.visibility = View.VISIBLE
        }
        binding.btnReturnSalesAppointLineOperation.setOnClickListener {
            binding.layoutMainSalesAppointLineOperation.openDrawer(binding.layout3SalesAppointLineOperation)

            isTakeOrder = false
            isError = true
            isComplete = false

            binding.tvTitle3SalesAppointLineOperation.text = "售后返单"

            binding.btnSaveSalesAppointLineOperation.text = "售后返单"

            binding.tvTitle4SalesAppointLineOperatin.visibility = View.GONE
            binding.spToEmpSalesAppointLineOperation.visibility = View.GONE
        }
        binding.btnCancelSalesAppointLineOperation.setOnClickListener {
            binding.layoutMainSalesAppointLineOperation.openDrawer(binding.layout3SalesAppointLineOperation)

            isTakeOrder = false
            isError = false
            isComplete = true

            binding.tvTitle3SalesAppointLineOperation.text = "手动结单"

            binding.btnSaveSalesAppointLineOperation.text = "第三方跟进"

            binding.tvTitle4SalesAppointLineOperatin.visibility = View.GONE
            binding.spToEmpSalesAppointLineOperation.visibility = View.GONE
        }
        binding.btnOkSalesAppointLineOperation.setOnClickListener {
            binding.layoutMainSalesAppointLineOperation.openDrawer(binding.layout3SalesAppointLineOperation)

            isTakeOrder = true
            isError = false
            isComplete = false

            binding.tvTitle3SalesAppointLineOperation.text = "接单"

            binding.btnSaveSalesAppointLineOperation.text = "接单"

            binding.tvTitle4SalesAppointLineOperatin.visibility = View.GONE
            binding.spToEmpSalesAppointLineOperation.visibility = View.GONE
        }

        selectedSa = arguments?.getString("saId", "-1")?.toInt() ?: -1

        viewModel.getSalesAppointInfo(selectedSa)
        viewModel.getSalesAppointLineList(selectedSa)
        viewModel.getAppointUser()
    }

}
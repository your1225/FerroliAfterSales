package com.ferroli.after_sales.salesAppoint

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
import com.ferroli.after_sales.databinding.FragmentSalesAppointOperationBinding
import com.ferroli.after_sales.salesOrder.SalesOrderLineCellAdapter
import com.ferroli.after_sales.utils.DetailCellAdapter
import com.ferroli.after_sales.utils.ToastUtil
import java.util.ArrayList

class SalesAppointOperationFragment : Fragment() {

    private lateinit var binding: FragmentSalesAppointOperationBinding
    private val viewModel by viewModels<SalesAppointOperationViewModel>()

    private var selectedSo: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalesAppointOperationBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dAdapter = DetailCellAdapter()
        val solAdapter = SalesOrderLineCellAdapter()

        binding.rvSoDataSalesAppointOperation.apply {
            adapter = dAdapter
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }

        binding.rvSoLineSalesAppointOperation.apply {
            adapter = solAdapter
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }

        viewModel.detailInfoRecord.observe(viewLifecycleOwner) {
            dAdapter.submitList(it)
        }

        viewModel.soLineRecord.observe(viewLifecycleOwner) {
            solAdapter.submitList(it)
        }

        viewModel.remarkText.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                ToastUtil.showToast(requireContext(), it)
            }
        }

        viewModel.appointUserRecord.observe(viewLifecycleOwner) {
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

            binding.spToWhomSalesAppointOperation.adapter = adapter
        }

        viewModel.bscRecord.observe(viewLifecycleOwner) { bscList ->
            if (bscList == null)
                return@observe

            val gdList: MutableList<String> = ArrayList()

            for (item in bscList) {
                gdList.add(item.bScName)
            }

            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                gdList
            )

            binding.spBaseSupportCategorySalesAppointOperation.adapter = adapter
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

        binding.btnSaveSalesAppointOperation.setOnClickListener {
            val bscPosition: Int =
                binding.spBaseSupportCategorySalesAppointOperation.selectedItemPosition
            val toEmpPosition: Int = binding.spToWhomSalesAppointOperation.selectedItemPosition

            viewModel.saveData(
                bscPosition = bscPosition,
                toEmpPosition = toEmpPosition,
                soId = selectedSo
            )
        }

        selectedSo = arguments?.getString("soId", "-1")?.toInt() ?: -1

        viewModel.getSalesOrderInfo(selectedSo)
        viewModel.getSalesOrderLineList(selectedSo)

        viewModel.getAppointUser()
        viewModel.getBaseSupportCategory()
    }


}
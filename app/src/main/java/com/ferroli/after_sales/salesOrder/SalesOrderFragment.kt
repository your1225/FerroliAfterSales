package com.ferroli.after_sales.salesOrder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.ferroli.after_sales.R
import com.ferroli.after_sales.basePartInfo.BasePartInfoCellAdapter
import com.ferroli.after_sales.baseProductInfo.BaseProductInfoCellAdapter
import com.ferroli.after_sales.databinding.FragmentSalesOrderBinding
import com.ferroli.after_sales.entity.urlFileBase
import com.ferroli.after_sales.utils.ToastUtil

class SalesOrderFragment : Fragment() {

    private lateinit var binding: FragmentSalesOrderBinding
    private val viewModel by activityViewModels<SalesOrderViewModel>()

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

        val bPiCode = arguments?.getString("bPiCode", null)

        if (bPiCode != null && bPiCode != "null") {
            viewModel.addProductInfo(bPiCode)
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
    }
}
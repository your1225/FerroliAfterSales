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
import com.ferroli.after_sales.agentOrder.AgentOrderCellAdapter
import com.ferroli.after_sales.databinding.FragmentSalesFinishOperationBinding
import com.ferroli.after_sales.entity.AgentOrderLine
import com.ferroli.after_sales.entity.SalesFinishLine
import java.util.ArrayList

class SalesFinishOperationFragment : Fragment(), SalesFinishCellAdapter.OnItemOperationListener {

    private lateinit var binding: FragmentSalesFinishOperationBinding
    private val viewModel by activityViewModels<SalesFinishOperationViewModel>()
    private val mmAdapter = SalesFinishCellAdapter(this)

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

        viewModel.getBaseServiceType()
    }

    override fun onDeleteClick(item: SalesFinishLine) {
        viewModel.removeBasePartInfo(item)
    }
}
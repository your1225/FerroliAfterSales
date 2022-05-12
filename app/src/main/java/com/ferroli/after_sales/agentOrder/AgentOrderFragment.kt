package com.ferroli.after_sales.agentOrder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ferroli.after_sales.R
import com.ferroli.after_sales.databinding.AgentOrderFragmentBinding
import com.ferroli.after_sales.entity.AgentOrderLine
import com.ferroli.after_sales.utils.LoginInfo
import com.ferroli.after_sales.utils.ToastUtil

class AgentOrderFragment : Fragment(), AgentOrderCellAdapter.OnItemOperationListener {

    private lateinit var binding: AgentOrderFragmentBinding
    // private val viewModel by viewModels<AgentOrderViewModel>()
    // 如果需要Fragment之间共享ViewModel则用下面的方式 activityViewModels 如果只是一个 Fragment 那就还是用 viewModels
    private val viewModel by activityViewModels<AgentOrderViewModel>()
    private val mmAdapter = AgentOrderCellAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AgentOrderFragmentBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refreshGeneralMenu)?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvDataAgentOrder.apply {
            adapter = mmAdapter
            layoutManager = object : LinearLayoutManager(requireContext()){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
        binding.btnAddPartAgentOrder.setOnClickListener {
            findNavController().navigate(
                R.id.action_agentOrderFragment_to_basePartInfoFragment
            )
        }

        viewModel.basePartInfoRecord.observe(viewLifecycleOwner) {
            mmAdapter.submitList(it)
        }
        viewModel.aoReceiveName.observe(viewLifecycleOwner) {
            binding.tbAOReceiveNameAgentOrder.setText(it)
        }
        viewModel.aoReceiveTel.observe(viewLifecycleOwner) {
            binding.tbAOReceiveTelAgentOrder.setText(it)
        }
        viewModel.aoReceiveAddress.observe(viewLifecycleOwner) {
            binding.tbAOReceiveAddressAgentOrder.setText(it)
        }
        viewModel.aoRemark.observe(viewLifecycleOwner) {
            binding.tbAORemarkAgentOrder.setText(it)
        }
        viewModel.remarkText.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                ToastUtil.showToast(requireContext(), it)

                binding.tvRemarkAgentOrder.text = it
            } else {
                binding.tvRemarkAgentOrder.text = ""
            }
        }

        viewModel.getLastInfo(LoginInfo.getLoginEmpId(requireContext()))
    }

    override fun OnDeleteClick(item: AgentOrderLine) {
        viewModel.removeBasePartInfo(item)
    }
}
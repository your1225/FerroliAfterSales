package com.ferroli.after_sales.agentOrder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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
            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
        binding.btnAddPartAgentOrder.setOnClickListener {
            val bundle = bundleOf(
                "FromFragment" to "AgentOrder"
            )

            findNavController().navigate(
                R.id.action_agentOrderFragment_to_basePartInfoFragment,
                bundle
            )
        }
        binding.btnSaveAgentOrder.setOnClickListener {
            if (viewModel.basePartInfoRecord.value.isNullOrEmpty()) {
                val msg = "请先选择配件"

                ToastUtil.showToast(requireContext(), msg)

                binding.tvRemarkAgentOrder.text = msg

                return@setOnClickListener
            }

            val receiveName = binding.tbAOReceiveNameAgentOrder.text.toString()
            val receiveTel = binding.tbAOReceiveTelAgentOrder.text.toString()
            val receiveAddress = binding.tbAOReceiveAddressAgentOrder.text.toString()
            val remark = binding.tbAORemarkAgentOrder.text.toString()

            viewModel.saveData(
                receiveName = receiveName,
                receiveTel = receiveTel,
                receiveAddress = receiveAddress,
                remark = remark
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
        viewModel.reData.observe(viewLifecycleOwner) {
            when (it?.fOK) {
                "True" -> {
//                    Log.e("ferroli_log",
//                        "reData observe " + viewModel.ckNoLiveData.value + " - " + viewModel.itmLiveData.value)

                    viewModel.clearData()
                }
                "False" -> {
                    binding.tvRemarkAgentOrder.text = it.fMsg
                }
            }
        }
        viewModel.remarkText.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                ToastUtil.showToast(requireContext(), it)

                binding.tvRemarkAgentOrder.text = it
            } else {
                binding.tvRemarkAgentOrder.text = ""
            }
        }

        if (binding.tbAOReceiveNameAgentOrder.text.toString().isEmpty()){
            viewModel.getLastInfo()
        }
    }

    override fun onDeleteClick(item: AgentOrderLine) {
        viewModel.removeBasePartInfo(item)
    }
}
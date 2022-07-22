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

        val appointLevel = LoginInfo.getLoginEmpAppointLevel(requireContext())

        if (appointLevel > 3) {
            binding.btnAddPartAgentOrder.visibility = View.INVISIBLE
            binding.btnSaveAgentOrder.visibility = View.INVISIBLE

            ToastUtil.showToast(requireContext(), "配件订购，必须使用网点的账号！")
        }

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
                ToastUtil.showToast(requireContext(), "请先选择配件")

                return@setOnClickListener
            }

            val receiveName = binding.tbAOReceiveNameAgentOrder.text.toString()
            val receiveTel = binding.tbAOReceiveTelAgentOrder.text.toString()
            val receiveAddress = binding.tbAOReceiveAddressAgentOrder.text.toString()
            val remark = binding.tbAORemarkAgentOrder.text.toString()

            if (receiveName.isEmpty() || receiveTel.isEmpty() || receiveAddress.isEmpty()) {
                ToastUtil.showToast(requireContext(), "必须填入用户姓名、电话、地址！")

                return@setOnClickListener
            }

            if (binding.tvSurplusMoneyAgentOrder.text.toString().isNotEmpty()) {
                val surplusMoney = binding.tvSurplusMoneyAgentOrder.text.toString().toDouble()

                if (surplusMoney < 0) {
                    ToastUtil.showToast(requireContext(), "您订购的配件已大于您的余额，请先充值！")

                    return@setOnClickListener
                }
            }

            viewModel.saveData(
                receiveName = receiveName,
                receiveTel = receiveTel,
                receiveAddress = receiveAddress,
                remark = remark
            )
        }

        viewModel.basePartInfoRecord.observe(viewLifecycleOwner) { it ->
            mmAdapter.submitList(it)

            if (it != null) {
                val sumMoney = it.sumOf { line ->
                    line.aOlCount * line.aOlAgentPrice.toDouble()
                }

                val surplusMoney = viewModel.agentBalance.value?.minus(sumMoney)

                binding.tvSumMoneyAgentOrder.text = sumMoney.toString()
                binding.tvSurplusMoneyAgentOrder.text = surplusMoney.toString()
            } else {
                binding.tvSumMoneyAgentOrder.text = "0"
                binding.tvSurplusMoneyAgentOrder.text = viewModel.agentBalance.value.toString()
            }
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
        viewModel.agentBalance.observe(viewLifecycleOwner) {
            binding.tvAgentBalanceAgentOrder.text = it.toString()
        }
        viewModel.reData.observe(viewLifecycleOwner) {
            when (it?.fOK) {
                "True" -> {
//                    Log.e("ferroli_log",
//                        "reData observe " + viewModel.ckNoLiveData.value + " - " + viewModel.itmLiveData.value)

                    viewModel.clearData()

                    viewModel.getAgentBalance()

                    viewModel.remarkText.value = resources.getString(R.string.app_save_passed)
                }
                "False" -> {
                    viewModel.remarkText.value = it.fMsg
                }
            }
        }
        viewModel.remarkText.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                ToastUtil.showToast(requireContext(), it)
            }
        }

        viewModel.getAgentBalance()

        if (binding.tbAOReceiveNameAgentOrder.text.toString().isEmpty()) {
            viewModel.getLastInfo()
        }
    }

    override fun onDeleteClick(item: AgentOrderLine) {
        viewModel.removeBasePartInfo(item)
    }
}
package com.ferroli.after_sales.basePartInfo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.ferroli.after_sales.R
import com.ferroli.after_sales.agentOrder.AgentOrderViewModel
import com.ferroli.after_sales.databinding.BasePartInfoFragmentBinding
import com.ferroli.after_sales.entity.urlFileBase
import com.ferroli.after_sales.utils.ToastUtil

class BasePartInfoFragment : Fragment(), BasePartInfoCellAdapter.OnItemCheckedListener {

    private lateinit var binding: BasePartInfoFragmentBinding
    private val viewModel by viewModels<BasePartInfoViewModel>()
    private val viewModelAgentOrder by activityViewModels<AgentOrderViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BasePartInfoFragmentBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refreshGeneralMenu)?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onItemChecked(position: Int) {
        viewModel.selectedItem.value = viewModel.basePartInfoRecord.value?.get(position)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshState()

        val mmAdapter = BasePartInfoCellAdapter(this)

        binding.rvDataBasePartInfo.apply {
            adapter = mmAdapter
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }

        viewModel.basePartInfoRecord.observe(viewLifecycleOwner) {
            mmAdapter.submitList(it)
        }
        viewModel.remarkText.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                ToastUtil.showToast(requireContext(), it)
            }
        }
        viewModel.selectedItem.observe(viewLifecycleOwner) {
            binding.layoutBasePartInfo.openDrawer(binding.layout2BasePartInfo)

            binding.tvBPcNameBasePartInfo.text = it.bPcName
            binding.tvbPaiCodeBasePartInfo.text = it.bPaiCode
            binding.tvBPaiNameBasePartInfo.text = it.bPaiName
            binding.tvBPaiAgentPriceBasePartInfo.text = it.bPaiAgentPrice.toString()

            Glide.with(requireContext())
                .load(urlFileBase + it.bPaiCode + ".jpg")
                .centerCrop()
                .placeholder(R.drawable.ic_default_part_img)
                .into(binding.imageViewBasePartInfo)
        }

        binding.btnSearchBasePartInfo.setOnClickListener {
            val searchStr: String = binding.tvSearchStringBasePartInfo.text.toString()

            if (searchStr.isNotBlank()) {
                viewModel.getSearchData(searchStr)

                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.tvSearchStringBasePartInfo.windowToken, 0)
            } else {
                viewModel.remarkText.value = "必须填入查询信息"
            }
        }
        binding.imageViewBasePartInfo.setOnClickListener {
            viewModel.selectedItem.value?.run {
                val bundle = bundleOf(
                    "PartCode" to this.bPaiCode
                )

                findNavController().navigate(
                    R.id.action_basePartInfoFragment_to_partImageViewFragment,
                    bundle
                )
            }
        }
        binding.btnSaveBasePartInfo.setOnClickListener {
            if (viewModel.selectedItem.value != null) {
                viewModel.selectedItem.value?.run {
                    val count: Int = binding.tvCountBasepartInfo.text.toString().toInt()

                    viewModelAgentOrder.addBasePartInfo(this, count)

                    viewModel.remarkText.value = "添加完成"

                    refreshState()
                }
            } else {
                viewModel.remarkText.value = "未选择任何物料信息"
            }
        }
    }

    private fun refreshState() {
        binding.tvCountBasepartInfo.setText("1")

        viewModelAgentOrder.basePartInfoRecord.value?.run {
            binding.tvSelectedCountBasepartInfo.text =
                this.size.toString()
        }

    }
}
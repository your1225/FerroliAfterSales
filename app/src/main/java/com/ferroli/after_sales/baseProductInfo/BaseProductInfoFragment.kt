package com.ferroli.after_sales.baseProductInfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.ferroli.after_sales.R
import com.ferroli.after_sales.databinding.FragmentBaseProductInfoBinding
import com.ferroli.after_sales.entity.urlFileBase
import com.ferroli.after_sales.salesOrder.SalesOrderViewModel
import com.ferroli.after_sales.utils.LoginInfo
import com.ferroli.after_sales.utils.ToastUtil

class BaseProductInfoFragment : Fragment(), BaseProductInfoCellAdapter.OnItemCheckedListener {
    private lateinit var binding: FragmentBaseProductInfoBinding
    private val viewModel by viewModels<BaseProductInfoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseProductInfoBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refreshGeneralMenu)?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onItemChecked(position: Int) {
        val bPiCode = viewModel.baseProductInfoRecord.value?.get(position)?.bPiCode

//        val bundle = bundleOf(
//            "bPiCode" to bPiCode
//        )

        findNavController().previousBackStackEntry?.savedStateHandle?.set("bPiCode", bPiCode)

        findNavController().popBackStack()

//        findNavController().navigate(
//            R.id.action_baseProductInfoFragment_to_salesOrderFragment,
//            bundle
//        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mmAdapter = BaseProductInfoCellAdapter(this)

        binding.rvDataBaseProductInfo.apply {
            adapter = mmAdapter
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }

        viewModel.baseProductInfoRecord.observe(viewLifecycleOwner) {
            mmAdapter.submitList(it)
        }
        viewModel.remarkText.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                ToastUtil.showToast(requireContext(), it)
            }
        }

        binding.btnSearchBaseProductInfo.setOnClickListener {
            val searchStr: String = binding.tvSearchStringBaseProductInfo.text.toString()

            if (searchStr.isNotBlank()) {
                viewModel.getSearchData(searchStr)
            } else {
                viewModel.remarkText.value = "必须填入查询信息"
            }
        }
    }


}
package com.ferroli.after_sales.salesAppoint

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ferroli.after_sales.R
import com.ferroli.after_sales.databinding.FragmentSalesAppointBinding
import com.ferroli.after_sales.salesOrder.SalesOrderCellAdapter
import com.ferroli.after_sales.utils.ToastUtil

class SalesAppointFragment : Fragment(), SalesOrderCellAdapter.OnItemCheckedListener {

    private lateinit var binding: FragmentSalesAppointBinding
    private val viewModel by viewModels<SalesAppointViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalesAppointBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        this.searchData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mmAdapter = SalesOrderCellAdapter(this)

        binding.rvDataSalesAppoint.apply {
            adapter = mmAdapter
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }

        binding.tvSearchStringSalesAppoint.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                this.searchData()
            }

            false
        }

        binding.textInputLayoutSalesAppoint.setEndIconOnClickListener {
            this.searchData()
        }

        viewModel.salesOrderRecord.observe(viewLifecycleOwner) {
            mmAdapter.submitList(it)
        }

        viewModel.remarkText.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                ToastUtil.showToast(requireContext(), it)
            }
        }
    }

    private fun searchData() {
        val telStr = binding.tvSearchStringSalesAppoint.text.toString()

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.tvSearchStringSalesAppoint.windowToken, 0)

        viewModel.getSalesOrderList(telStr)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refreshGeneralMenu)?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onItemChecked(position: Int) {
        val soId = viewModel.salesOrderRecord.value?.get(position)?.soId

        val bundle = bundleOf(
            "soId" to soId.toString()
        )

        findNavController().navigate(
            R.id.action_salesAppointFragment_to_salesAppointOperationFragment,
            bundle
        )
    }
}
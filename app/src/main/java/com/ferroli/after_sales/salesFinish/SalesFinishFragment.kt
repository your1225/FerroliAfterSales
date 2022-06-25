package com.ferroli.after_sales.salesFinish

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ferroli.after_sales.R
import com.ferroli.after_sales.databinding.FragmentSalesFinishBinding
import com.ferroli.after_sales.salesAppoint.SalesAppointCellAdapter
import com.ferroli.after_sales.utils.ToastUtil

class SalesFinishFragment : Fragment(), SalesAppointCellAdapter.OnItemCheckedListener {

    private lateinit var binding: FragmentSalesFinishBinding
    private val viewModel by viewModels<SalesFinishViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalesFinishBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mmAdapter = SalesAppointCellAdapter(this)

        binding.rvDataSalesFinish.apply {
            adapter = mmAdapter
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }

        binding.tvSearchStringSalesFinish.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                this.searchData()
            }

            false
        }

        binding.textInputLayoutSalesFinish.setEndIconOnClickListener {
            this.searchData()
        }

        viewModel.salesAppointLineRecord.observe(viewLifecycleOwner) {
            mmAdapter.submitList(it)
        }

        viewModel.remarkText.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                ToastUtil.showToast(requireContext(), it)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        this.searchData()
    }

    private fun searchData() {
        val telStr = binding.tvSearchStringSalesFinish.text.toString()

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.tvSearchStringSalesFinish.windowToken, 0)

        viewModel.getModelListTook(telStr)
    }

    override fun onItemChecked(position: Int) {
        val saId = viewModel.salesAppointLineRecord.value?.get(position)?.saId

        val bundle = bundleOf(
            "saId" to saId.toString()
        )

        findNavController().navigate(
            R.id.action_salesFinishFragment_to_salesFinishOperationFragment,
            bundle
        )
    }
}
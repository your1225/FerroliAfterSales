package com.ferroli.after_sales.salesAppointLine

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ferroli.after_sales.R
import com.ferroli.after_sales.databinding.FragmentSalesAppointLineBinding
import com.ferroli.after_sales.salesAppoint.SalesAppointCellAdapter
import com.ferroli.after_sales.utils.ToastUtil

class SalesAppointLineFragment : Fragment(), SalesAppointCellAdapter.OnItemCheckedListener {

    private lateinit var binding: FragmentSalesAppointLineBinding
    private val viewModel by viewModels<SalesAppointLineViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalesAppointLineBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mmAdapter = SalesAppointCellAdapter(this)

        binding.rvDataSalesAppointLine.apply {
            adapter = mmAdapter
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }

        binding.tvSearchStringSalesAppointLine.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                this.searchData()
            }

            false
        }

        binding.textInputLayout1SalesAppointLine.setEndIconOnClickListener {
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
        val telStr = binding.tvSearchStringSalesAppointLine.text.toString()

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.tvSearchStringSalesAppointLine.windowToken, 0)

        viewModel.getSalesAppointToMeList(telStr)
    }

    override fun onItemChecked(position: Int) {
        val saId = viewModel.salesAppointLineRecord.value?.get(position)?.saId

        val bundle = bundleOf(
            "saId" to saId.toString()
        )

        findNavController().navigate(
            R.id.action_salesAppointLineFragment_to_salesAppointLineOperationFragment,
            bundle
        )
    }
}
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
import com.bumptech.glide.Glide
import com.ferroli.after_sales.R
import com.ferroli.after_sales.databinding.FragmentSalesOrderBinding
import com.ferroli.after_sales.entity.urlFileBase

class SalesOrderFragment : Fragment() {

    private lateinit var binding: FragmentSalesOrderBinding
    private val viewModel by activityViewModels<SalesOrderViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalesOrderBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bPiCode = arguments?.getString("bPiCode", null)

        if (bPiCode != null && bPiCode != "null") {
//            viewModel.addProductInfo(bPiCode)
        }

        val list: MutableList<String> = ArrayList()

        for (i: Int in 1..100)
            list.add("Item $i")

        val adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            list
        )

        binding.spnProvinceSalesOrder.adapter = adapter

        binding.spnProvinceSalesOrder.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    // p2 是所选 position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }
}
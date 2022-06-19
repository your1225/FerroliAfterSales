package com.ferroli.after_sales.salesAppointLine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ferroli.after_sales.databinding.FragmentSalesAppointLineOperationBinding

class SalesAppointLineOperationFragment : Fragment() {

    private lateinit var binding: FragmentSalesAppointLineOperationBinding
    private val viewModel by viewModels<SalesAppointLineOperationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalesAppointLineOperationBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.functionButtonView.setOnClickListener {

        }
    }

}
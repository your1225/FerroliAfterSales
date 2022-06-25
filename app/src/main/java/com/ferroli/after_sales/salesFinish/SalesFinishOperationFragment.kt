package com.ferroli.after_sales.salesFinish

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ferroli.after_sales.R
import com.ferroli.after_sales.databinding.FragmentSalesFinishBinding
import com.ferroli.after_sales.databinding.FragmentSalesFinishOperationBinding

class SalesFinishOperationFragment : Fragment() {

    private lateinit var binding: FragmentSalesFinishOperationBinding
    private val viewModel by viewModels<SalesFinishOperationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalesFinishOperationBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}